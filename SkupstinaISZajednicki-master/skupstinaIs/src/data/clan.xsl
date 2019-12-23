<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
 xmlns:xlink="http://www.w3.org/1999/xlink">
 
	 <xsl:template match="/document">
	    <html>
	      <head>
	        <title>
	          <xsl:value-of select="header/title"/> by <xsl:value-of select="header/author"/>
	        </title>
	      </head>
	      <body>
	        <h1><xsl:value-of select="header/title"/></h1>
	        <p>by <xsl:value-of select="header/author"/></p>
	        <xsl:apply-templates select="body/para"/>
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