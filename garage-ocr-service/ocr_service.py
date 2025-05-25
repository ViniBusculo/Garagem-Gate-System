# ocr_service.py

from flask import Flask, request, jsonify
import easyocr
import os

app = Flask(__name__)
reader = easyocr.Reader(['en'])  # Pode adicionar 'pt' também se desejar

@app.route('/ocr', methods=['POST'])
def ocr():
    if 'file' not in request.files:
        return jsonify({'error': 'Arquivo não enviado'}), 400

    file = request.files['file']
    file_path = os.path.join("temp", file.filename)
    file.save(file_path)

    result = reader.readtext(file_path)
    os.remove(file_path)

    placa = None
    for detection in result:
        text = detection[1].replace(" ", "").upper()
        if len(text) == 7 and all(c.isalnum() for c in text):
            placa = text
            break

    if placa:
        return jsonify({'placa': placa})
    else:
        return jsonify({'placa': None, 'mensagem': 'Placa não reconhecida'}), 200

if __name__ == '__main__':
    os.makedirs("temp", exist_ok=True)
    app.run(host='0.0.0.0', port=5000)
