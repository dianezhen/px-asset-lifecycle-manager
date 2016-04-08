package com.ge.predix.alm.repository;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.cloud.service.ServiceInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;


public class BlobStoreRepository {
	
	@Autowired
	private ServiceInfo blobstoreServiceInfo;

	public String save(String schema){
		//HttpURLConnection connection = (HttpURLConnection) new URL(blobstoreServiceInfo.getUri()).openConnection();
		// set some connection properties
//		OutputStream output = connection.getOutputStream();
//		PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, CHARSET), true); 
		// set some headers with writer
		
		//InputStream inputStream = file.getInputStream();
		//long fileSize = file.getSize();
		
		//try {
			//byte[] buffer = new byte[4096];
			//int length;
			//w/hile ((length = inputStream.read(buffer)) > 0) {
				//output.write(buffer,0,length);
			//}
			//output.flush();
			//writer.flush();
	//	}

		return "TO BE IMPLEMENTED";
	}
	
	public String getSchema(String uri) {
		/*
		HttpURLConnection connection = (HttpURLConnection ) new URL(url).openConnection();
		// set some connection properties
		InputStream input = connection.getInputStream();
		
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, CHARSET), true); 
		// set some headers with writer
		
		InputStream inputStream = file.getInputStream();
		long fileSize = file.getSize();
		
		try {
			byte[] buffer = new byte[4096];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				output.write(buffer,0,length);
			}
			output.flush();
			writer.flush();
		}
		*/
		//TO BE IMPLEMENTED
		return "";
	}
}