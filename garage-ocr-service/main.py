# from fastapi import FastAPI
# from fastapi.responses import JSONResponse
# import cv2
# import easyocr
# import numpy as np
# import re

# app = FastAPI()
# reader = easyocr.Reader(['pt', 'en'])

# URL_CAMERA = "http://192.168.0.8:8080/video"

# @app.get("/ler-placa")
# def ler_placa():
#     cap = cv2.VideoCapture(URL_CAMERA)

#     if not cap.isOpened():
#         return JSONResponse(content={"mensagem": "Erro ao acessar a câmera."}, status_code=500)

#     ret, frame = cap.read()
#     cap.release()

#     if not ret:
#         return JSONResponse(content={"mensagem": "Erro ao capturar frame do vídeo."}, status_code=500)

#     resultados = reader.readtext(frame)
#     padrao_placa = re.compile(r'^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$')

#     placas = [
#         r[1].replace(" ", "").upper()
#         for r in resultados
#         if padrao_placa.fullmatch(r[1].replace(" ", "").upper())
#     ]

#     if not placas:
#         return JSONResponse(content={"mensagem": "Placa não detectada."}, status_code=400)

#     placa_detectada = placas[0]
#     return {"placa": placa_detectada}

import cv2
import easyocr
import re
import requests
import time
import numpy as np

reader = easyocr.Reader(['pt', 'en'])
URL_CAMERA = "http://192.168.0.8:8080/video"
URL_JAVA_VERIFICAR = "http://localhost:8080/api/placa/verificar?placa="

placa_regex = re.compile(r'^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$')
ultimas_placas = {}
INTERVALO_REPETICAO = 10  # segundos
INTERVALO_RECONEXAO = 5   # segundos

def abrir_camera(url):
    print("🔄 Conectando à câmera...")
    return cv2.VideoCapture(url)

cap = abrir_camera(URL_CAMERA)
ultimo_frame = None

while True:
    if not cap.isOpened():
        print("❌ Conexão com a câmera perdida. Tentando reconectar...")
        cap.release()
        time.sleep(INTERVALO_RECONEXAO)
        cap = abrir_camera(URL_CAMERA)
        continue

    ret, frame = cap.read()
    if not ret or frame is None:
        print("⚠️ Stream não retornou frame válido. Tentando novamente...")
        time.sleep(1)
        continue

    if ultimo_frame is not None and np.array_equal(ultimo_frame, frame):
        # Frame congelado
        print("🧊 Frame congelado, aguardando nova imagem...")
        time.sleep(1)
        continue

    ultimo_frame = frame.copy()
    resultados = reader.readtext(frame)

    for (_, texto, _) in resultados:
        texto = texto.replace(" ", "").upper()
        if not placa_regex.fullmatch(texto):
            continue

        agora = time.time()
        if texto in ultimas_placas and (agora - ultimas_placas[texto] < INTERVALO_REPETICAO):
            continue  # Ignora repetições próximas

        print(f"Placa detectada: {texto}")
        try:
            resposta = requests.get(URL_JAVA_VERIFICAR + texto)
            if resposta.status_code == 200:
                autorizada = resposta.json().get("autorizada", False)
                if autorizada:
                    print(f"✅ Acesso liberado para: {texto}")
                else:
                    print(f"❌ Acesso negado para: {texto}")

            # 🔁 Envia log para o Java
            try:
                requests.post("http://localhost:8080/api/placa/logar-acesso", json={
                    "placa": texto,
                    "autorizado": autorizada
                })
            except Exception as e:
                print("Erro ao enviar log de acesso:", e)
            else:
                print("Erro ao verificar placa com API Java")
        except Exception as e:
            print("Erro na requisição:", e)

        ultimas_placas[texto] = agora

    time.sleep(1)
