<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink"
xmlns="http://www.parlament.gov.rs/propis" xmlns:ns2="http://www.parlament.gov.rs/clan" xmlns:ns3="http://www.parlament.gov.rs/amandman">

	<xsl:template match="/">
	 <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="amandman-page">
                    <fo:region-body margin="1in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            
            <fo:page-sequence master-reference="amandman-page">
                <fo:flow flow-name="xsl-region-body">
                
                	 <fo:block font-family="sans-serif" font-size="10px" font-weight="bold" padding="2px">
                        Skupstina republike Srbije
                     </fo:block>
                
                	 <fo:block font-family="sans-serif" font-size="10px" font-weight="bold" padding="2px">
                        <xsl:value-of select="ns3:Amandman/@Mesto"/>
                     </fo:block>
                    
                    <fo:block font-family="sans-serif" font-size="10px" font-weight="bold" padding="2px">
                        <xsl:value-of select="ns3:Amandman/@Datum"/>
                    </fo:block>
                	
                    <fo:block font-family="sans-serif" text-indent="150px" font-size="20px" font-weight="bold" padding="2px">
                        Amandman <xsl:value-of select="ns3:Amandman/@Redni_broj"/>
                    </fo:block>
                    
                     <fo:block font-family="sans-serif" text-indent="200px" font-size="1px" padding="2px">
                         .
                    </fo:block>
                   
                    <fo:block text-indent="180px" linefeed-treatment="preserve">
                    	Resenje:
                    	<xsl:value-of select="ns3:Amandman/ns3:Resenje"/>
                    </fo:block>
                    
                    <fo:block font-family="sans-serif" text-indent="200px" font-size="1px" padding="2px">
                         .
                    </fo:block>
                    
                     <fo:block font-family="sans-serif" text-indent="200px" font-size="1px" padding="2px">
                         .
                    </fo:block>
                    
                    
                    
                    <fo:block text-indent="165px" linefeed-treatment="preserve">
                    	Obrazlozenje:
                    	<xsl:value-of select="ns3:Amandman/ns3:Obrazlozenje"/>
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
                    	 Narodni poslanik:
                    	 <xsl:value-of select="ns3:Amandman/@Podnosilac"/>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
	</xsl:template>
</xsl:stylesheet>