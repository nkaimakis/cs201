package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HTTPResponse {
	
	private static final int BUFFER_SIZE = 1024;
	OutputStream output;
	HTTPRequest request;
	WebServer owner;

	public HTTPResponse(OutputStream output, WebServer owningServer) {
		//Set the output stream so the reponse object knows where to send its output.
		this.output = output;
		this.owner = owningServer;
	}
	
	public void setRequest(HTTPRequest request) {
		this.request = request;
		try {
			int responsecode = sendStaticResource();
			if (responsecode != 0) {
				// an error occurred in the resource request, output appropriate error message
				returnError(responsecode);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public int sendStaticResource() throws IOException {
		int returncode = 0;
		byte[] bytes = new byte[BUFFER_SIZE];
		FileInputStream fis = null;
		try {
			File requestedfile = new File(owner.getDocRoot(), request.getURI());
			if (requestedfile.exists()) {
				//read in file
				fis = new FileInputStream(requestedfile);
				int ch = fis.read(bytes, 0, BUFFER_SIZE);
				//output bytes
				while (ch != -1) {
					output.write(bytes, 0, ch);
					ch = fis.read(bytes, 0, BUFFER_SIZE);
				}
			} else {
				// file not found
				returncode = 404;
			}
		} catch (Exception e) {
			// thrown if cannot instantiate a File object
			System.out.println(e.toString());
		} finally {
			if (fis != null)
				fis.close();
		}
		return returncode;
	}
	
	public void returnError(int errornum) throws Exception {
		String errorNumber;
		String errorDetail;
		switch (errornum) {
		case 404:
			errorNumber = "HTTP/1.1 404 File Not Found\r\n";
			errorDetail = "<h1>Error processing request</h1><h2>Error 404 - File Not Found</h2>";
			break;
		case 501:
			errorNumber = "HTTP/1.1 501 Method Not Supported\r\n";
			errorDetail = "<h1>Error processing request</h1><h2>Error 501 - Requested Method is not supported by this HTTP Server</h2>";
			break;
		default:
			errorNumber = "HTTP/1.1 Unknown Error Number\r\n";
			errorDetail = "<h1>Error processing request</h1><h2>Sorry, Server has encountered an unexpected error</h2>";
			break;
		}
		String errorMessage = errorNumber + "Content-Type: text/html\r\n" + "Content-Length: " + errorDetail.length() + "\r\n" + "\r\n" + errorDetail;
		output.write(errorMessage.getBytes());
	}

}
