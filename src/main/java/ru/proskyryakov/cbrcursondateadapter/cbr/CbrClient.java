package ru.proskyryakov.cbrcursondateadapter.cbr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import org.springframework.ws.soap.client.core.SoapActionCallback;
import ru.proskuryakov.cbrcursondateadapter.cbr.wsdl.GetCursOnDateXML;
import ru.proskuryakov.cbrcursondateadapter.cbr.wsdl.GetCursOnDateXMLResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

public class CbrClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(CbrClient.class);
    @Value("${cbr.uri}")
    private String cbrUri;

    public GetCursOnDateXMLResponse getCursOnDateXMLResponse(GregorianCalendar date){
        GetCursOnDateXML request  = new GetCursOnDateXML();

        try {
            XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
            log.info(calendar.toXMLFormat());
            request.setOnDate(calendar);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return (GetCursOnDateXMLResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback("http://web.cbr.ru/GetCursOnDateXML"));

    }

}
