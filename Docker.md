

To package the client and server in Docker containers:

* Copy the MtClient.class and ClientListener.class files to the client subdirectory
* Copy the MtServer.class and ClientHandler.class files to the server subdirectory

* In the client subdirectory, to create the client Docker image use:
	docker image build -t client .

* In the server subdirectory, to create the server Docker image use:
	docker image build -t server .
