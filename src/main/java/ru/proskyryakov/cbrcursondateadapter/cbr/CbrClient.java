package ru.proskyryakov.cbrcursondateadapter.cbr;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import org.springframework.ws.soap.client.core.SoapActionCallback;
import ru.proskuryakov.cbrcursondateadapter.cbr.wsdl.GetCursOnDateXML;
import ru.proskuryakov.cbrcursondateadapter.cbr.wsdl.GetCursOnDateXMLResponse;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

public class CbrClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(CbrClient.class);

    @SneakyThrows
    public GetCursOnDateXMLResponse getCursOnDateXMLResponse(GregorianCalendar date){
        GetCursOnDateXML request  = new GetCursOnDateXML();
        XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
        request.setOnDate(calendar);

        return (GetCursOnDateXMLResponse) getResponse(request, "http://web.cbr.ru/GetCursOnDateXML");
    }

    public Object getResponse(Object request, String soapAction) {
        log.info("Execute request " + soapAction);
        return getWebServiceTemplate().marshalSendAndReceive(request, new SoapActionCallback(soapAction));
    }

}
