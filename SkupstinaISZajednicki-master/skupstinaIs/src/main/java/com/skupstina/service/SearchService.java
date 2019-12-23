package com.skupstina.service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;
import com.skupstina.db.DatabaseManager;
import com.skupstina.model.Amandman;
import com.skupstina.model.Amandmani;
import com.skupstina.model.Propis;
import com.skupstina.model.Propisi;

@Service
public class SearchService {

	@Autowired
	private DatabaseManager databaseManager;

	@Autowired
	private AmandmanService amandmanService;

	@Autowired
	private PropisService propisService;

	private static TransformerFactory transformerFactory;

	static {
		transformerFactory = TransformerFactory.newInstance();
	}

	public List<Amandman> searchAmandmani(String json) throws JAXBException {
		JSONObject searchEntity = new JSONObject(json);

		String collId = "/amandmani";

		String usvojenost = searchEntity.getString("usvojenost");
		String mesto = searchEntity.getString("mesto");
		String datum = searchEntity.getString("datum");
		String redniBroj = searchEntity.getString("redniBroj");
		String podnosilac = searchEntity.getString("podnosilac");
		String text = searchEntity.getString("text");
		System.out.println(podnosilac);
		QueryManager queryManager = databaseManager.getClient().newQueryManager();

		/*
		 * EditableNamespaceContext namespaces = new EditableNamespaceContext();
		 * namespaces.put("ns3", "http://www.parlament.gov.rs/amandman");
		 * 
		 * // Initialize structured query builder StructuredQueryBuilder
		 * queryBuilder = queryManager.newStructuredQueryBuilder();
		 * queryBuilder.setNamespaces(namespaces);
		 * 
		 * // Instantiate query definition StructuredQueryDefinition
		 * queryDefinition = queryBuilder.or(queryBuilder.term(podnosilac),
		 * queryBuilder.collection(collId));
		 * 
		 * // Perform search SearchHandle results =
		 * queryManager.search(queryDefinition, new SearchHandle()); //
		 * Serialize search results to the standard output
		 */
		StringQueryDefinition queryDefinition = queryManager.newStringDefinition();
		String upit = "";
		if (!usvojenost.equals("")) {
			upit += usvojenost + " AND ";
		}
		if (!mesto.equals("")) {
			upit += mesto + " AND ";
		}
		if (!datum.equals("")) {
			upit += datum + " AND ";
		}
		if (!redniBroj.equals("")) {
			upit += redniBroj + " AND ";
		}
		if (!podnosilac.equals("")) {
			upit += podnosilac + " AND ";
		}
		if (!upit.equals("")) {
			upit = upit.substring(0, upit.length() - 4);
		}
		System.out.println("Upit je " + text);
		String criteria = text;
		queryDefinition.setCriteria(criteria);
		queryDefinition.setCollections(collId);
		Amandmani amandmani = amandmanService.getAmandmani();
		List<Amandman> searchResult = new ArrayList<Amandman>();

		if (!criteria.equals("")) {
			SearchHandle results = queryManager.search(queryDefinition, new SearchHandle());
			MatchDocumentSummary matches[] = results.getMatchResults();

			MatchDocumentSummary result;

			for (int i = 0; i < matches.length; i++) {
				result = matches[i];
				String id = result.getUri().split("/")[2];
				for (Amandman a : amandmani.getAmandman()) {

					if (a.getID().toString().equals(id)) {
						searchResult.add(a);
					}
				}
			}
		}

		for (Amandman a : amandmani.getAmandman()) {
			if (!usvojenost.equals("")) {

			}
			if (!mesto.equals("")) {
				if (a.getMesto().toLowerCase().equals(mesto.toLowerCase())) {
					searchResult.add(a);
				}
			}
			if (a.getDatum() != null) {

				if (!datum.equals("")) {
					String dat = datum.substring(0, 10);
					dat += "Z";
					System.out.println("Datum " + a.getDatum().toString() + " == " + dat);
					if (dat.toString().equals(a.getDatum().toString())) {
						searchResult.add(a);
					}
				}
			}
			if (!redniBroj.equals("")) {
				if (a.getRedniBroj().toString().equals(redniBroj)) {
					searchResult.add(a);
				}
			}
			if (!podnosilac.equals("")) {
				if (a.getPodnosilac().toLowerCase().equals(podnosilac.toLowerCase())) {
					searchResult.add(a);
				}
			}
		}

		return searchResult;
	}

	public List<Propis> searchPropis(String json) throws JAXBException {
		JSONObject searchEntity = new JSONObject(json);

		String collId = "/propisi";

		String usvojenost = searchEntity.getString("usvojenost");
		String naziv = searchEntity.getString("naziv");
		String id = searchEntity.getString("id");
		String text = searchEntity.getString("text");
		String upit = "";
		if (!usvojenost.equals("")) {
			upit += usvojenost.toLowerCase() + " AND ";
		}
		if (!naziv.equals("")) {
			upit += naziv + " AND ";
		}
		if (!id.equals("")) {
			upit += id + " AND ";
		}
		if (!upit.equals("")) {
			upit = upit.substring(0, upit.length() - 4);
		}
		System.out.println("Upit je " + upit);
		QueryManager queryManager = databaseManager.getClient().newQueryManager();
		StringQueryDefinition queryDefinition = queryManager.newStringDefinition();
		String criteria = text;
		queryDefinition.setCriteria(criteria);
		queryDefinition.setCollections(collId);

		Propisi propisi = propisService.getPropisi();
		List<Propis> searchResult = new ArrayList<Propis>();
		
		if (!criteria.equals("")) {
			// Perform search
			SearchHandle results = queryManager.search(queryDefinition, new SearchHandle());
			// Serialize search results to the standard output
			MatchDocumentSummary matches[] = results.getMatchResults();

			MatchDocumentSummary result;

			for (int i = 0; i < matches.length; i++) {
				result = matches[i];
				String idd = result.getUri().split("/")[2];
				for (Propis a : propisi.getPropis()) {
					if (a.getID().toString().equals(idd)) {
						searchResult.add(a);
					}
				}
			}
		}
		for (Propis p : propisi.getPropis()) {
			if (!usvojenost.equals("") && p.getStatus() != null) {
				if (p.getStatus().equals(usvojenost.toLowerCase())) {
					searchResult.add(p);
				}
			}
			if (!naziv.equals("")) {
				if (p.getNaziv().equals(naziv.toLowerCase())) {
					searchResult.add(p);
				}
			}
			if (!id.equals("")) {
				if (p.getID().toString().equals(id)) {
					searchResult.add(p);
				}
			}
		}

		return searchResult;
	}

	/**
	 * Serializes DOM tree to an arbitrary OutputStream.
	 *
	 * @param node
	 *            a node to be serialized
	 * @param out
	 *            an output stream to write the serialized DOM representation to
	 * 
	 */
	private static void transform(Node node, OutputStream out) {
		try {

			// Kreiranje instance objekta zaduzenog za serijalizaciju DOM modela
			Transformer transformer = transformerFactory.newTransformer();

			// Indentacija serijalizovanog izlaza
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// Nad "source" objektom (DOM stablo) vrši se transformacija
			DOMSource source = new DOMSource(node);

			// Rezultujući stream (argument metode)
			StreamResult result = new StreamResult(out);

			// Poziv metode koja vrši opisanu transformaciju
			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
