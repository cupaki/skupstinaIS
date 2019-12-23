<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink"
xmlns:ns1="http://www.parlament.gov.rs/propis" xmlns:ns2="http://www.parlament.gov.rs/clan" xmlns:ns3="http://www.parlament.gov.rs/amandman" xmlns:ns4="http://www.parlament.gov.rs/akt">

	<xsl:template match="/">
	 <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="propis-page">
                    <fo:region-body margin="1in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            
            <fo:page-sequence master-reference="propis-page">
                <fo:flow flow-name="xsl-region-body">
                	
                    <fo:block font-family="sans-serif" text-indent="30px" font-size="20px" font-weight="bold" padding="2px">
                        <xsl:value-of select="ns1:Propis/ns1:Naziv"/>
                    </fo:block>
                     <fo:block font-family="sans-serif" text-indent="200px" font-size="1px" padding="2px">
                         .
                    </fo:block>
                     <fo:block font-family="sans-serif" text-indent="200px" font-size="1px" padding="2px">
                         .
                    </fo:block>
                     <fo:block font-family="sans-serif" text-indent="200px" font-size="1px" padding="2px">
                         .
                    </fo:block>
                     <fo:block font-family="sans-serif" text-indent="200px" font-size="1px" padding="2px">
                         .
                    </fo:block>
                    
                    <fo:block linefeed-treatment="preserve">
                    
                        <xsl:for-each select="ns1:Propis/ns1:Deo">
				            <fo:inline font-weight="bold" font-size="20pt"><xsl:value-of select="ns1:Naziv"/></fo:inline>:
				               
				               <xsl:for-each select="ns1:Glava">
				              		<xsl:value-of select="ns1:Naziv"/>:
				              		
				              		 	<xsl:for-each select="ns2:Clan">
				              				<xsl:value-of select="ns2:Naziv"/>:
				              				<xsl:value-of select="ns2:Opis"/>:
				             		
				             		 			<xsl:for-each select="ns2:Sadrzaj">
				              						<xsl:value-of select="ns2:Tekst"/>:
				              						
				              							<xsl:for-each select="ns2:Stav">
				              								Stav <xsl:value-of select="ns2:Redni_broj"/>:
				              								<xsl:value-of select="ns2:Tekst"/>
			          									</xsl:for-each>
				              						
			          							</xsl:for-each>
			          					</xsl:for-each>
			          			</xsl:for-each>
			          			
			          			<xsl:for-each select="ns2:Clan">
				              				<xsl:value-of select="ns2:Naziv"/>:
				              				<xsl:value-of select="ns2:Opis"/>:
				             		 
				             		 			<xsl:for-each select="ns2:Sadrzaj">
				              						<xsl:value-of select="ns2:Tekst"/>:
				              						
				              							<xsl:for-each select="ns2:Stav">
				              								Stav <xsl:value-of select="ns2:Redni_broj"/>:
				              								<xsl:value-of select="ns2:Tekst"/>:
			          									</xsl:for-each>
				              						
			          							</xsl:for-each>
			          			</xsl:for-each>
			          			
			          			
			          	
			          	</xsl:for-each>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
	</xsl:template>
</xsl:stylesheet>