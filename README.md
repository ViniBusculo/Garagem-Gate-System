# Garagem-Gate-System
Sistema de controle de acesso veicular por reconhecimento de placas. Este projeto permite a abertura automática do portão para veículos com placas previamente cadastradas. Todas as tentativas de acesso — autorizadas ou negadas — são registradas em um log para fins de auditoria.


Para rodar o projeto:

cd \garage-gate-api
mvn clean install
mvn package
java -jar target/garage-system-1.0.0.jar

Projeto Java Iniciado!

Vamos iniciar o Python.

cd \garage-ocr-servisse
uvicorn main:app --host 0.0.0.0 --port 8000

Python iniciado!

Precisa iniciar o servidor da câmera.

Estamos utilizando a câmera do celular.

No celular, baixar a app IP WebCam, dentro do app inicie o servidor. Precisa setar o IP recebido no código Python.
No Main.app, altera o IP
URL_CAMERA = "http://192.168.0.8:8080/video"
