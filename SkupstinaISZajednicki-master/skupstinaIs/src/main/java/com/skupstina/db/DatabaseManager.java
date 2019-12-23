package com.skupstina.db;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.skupstina.utils.Util;
import com.skupstina.utils.Util.ConnectionProperties;

@Service
public class DatabaseManager {

	private DatabaseClient client;
	
	public DatabaseManager() throws IOException {
		// Initialize the database client
		ConnectionProperties props = Util.loadProperties();
		if (props.database.equals("")) {
			System.out.println("[INFO] Using default database.");
			client = DatabaseClientFactory.newClient(props.host, props.port, props.user, props.password,
					props.authType);
		} else {
			System.out.println("[INFO] Using \"" + props.database + "\" database.");
			client = DatabaseClientFactory.newClient(props.host, props.port, props.database, props.user, props.password,
					props.authType);
		}
	}
	
	public DatabaseClient getClient() {
		return client;
	}

	public void setClient(DatabaseClient client) {
		this.client = client;
	}
	
	
}
