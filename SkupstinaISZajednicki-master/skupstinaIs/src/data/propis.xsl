<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink"
xmlns:ns1="http://www.parlament.gov.rs/propis" xmlns:ns2="http://www.parlament.gov.rs/clan" xmlns:ns3="http://www.parlament.gov.rs/amandman" xmlns:ns4="http://www.parlament.gov.rs/akt">
	
	<xsl:template match="/">
	    <html>
	      <head>
	        <title>
	          <xsl:value-of select="ns1:Propis/ns1:Naziv"/>
	        </title>
	      </head>
	      <body>
	        <h1><xsl:value-of select="ns1:Propis/ns1:Naziv"/></h1>
	        
		        <xsl:for-each select="ns1:Propis/ns1:Deo">
		              <h2><xsl:value-of select="ns1:Naziv"/></h2>
		              
		               <xsl:for-each select="ns1:Glava">
		              		<h3><xsl:value-of select="ns1:Naziv"/></h3>
		              		
		              		 	<xsl:for-each select="ns2:Clan">
		              				<h4><xsl:value-of select="ns2:Naziv"/></h4>
		              				<h5><xsl:value-of select="ns2:Opis"/></h5>
		             		 
		             		 			<xsl:for-each select="ns2:Sadrzaj">
		              						<h6><xsl:value-of select="ns2:Tekst"/></h6>
		              						
		              							<xsl:for-each select="ns2:Stav">
		              								<h6><xsl:value-of select="ns2:Redni_broj"/></h6>
		              								<h6><xsl:value-of select="ns2:Tekst"/></h6>
	          									</xsl:for-each>
		              						
	          							</xsl:for-each>
	          					</xsl:for-each>
	          			</xsl:for-each>
	          			
	          			<xsl:for-each select="ns2:Clan">
		              				<h4><xsl:value-of select="ns2:Naziv"/></h4>
		              				<h5><xsl:value-of select="ns2:Opis"/></h5>
		             		 
		             		 			<xsl:for-each select="ns2:Sadrzaj">
		              						<h6><xsl:value-of select="ns2:Tekst"/></h6>
		              						
		              							<xsl:for-each select="ns2:Stav">
		              								<h6><xsl:value-of select="ns2:Redni_broj"/></h6>
		              								<h6><xsl:value-of select="ns2:Tekst"/></h6>
	          									</xsl:for-each>
		              						
	          							</xsl:for-each>
	          			</xsl:for-each>
	          			
	          			
	          	
	          	</xsl:for-each>
	        
	        
	      </body>
	    </html>
	  </xsl:template>
	
	 <xsl:template match="jump">
   		 <xsl:element name="a">
     		 <xsl:attribute name="href">
       		 	<xsl:value-of select="@xlink:href"/>
      		 </xsl:attribute>
      		<xsl:apply-templates/>
    	</xsl:element>
 	 </xsl:template>
	
	
	
</xsl:stylesheet>