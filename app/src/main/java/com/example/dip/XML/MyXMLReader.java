package com.example.dip.XML;

import android.util.Log;

import com.example.dip.Classes.MyCurrencyClass;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyXMLReader
{
    private List<MyCurrencyClass> CurencyList = new ArrayList<>();

    private String XMLString;

    private String tagName, tempValue, CurrencyName;
    private Float Value;

    public MyXMLReader(String XMLstring)
    {
        XMLString = XMLstring;
        try {
            XmlPullParser xpp = prepareXpp();
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_TAG:
                        tagName = xpp.getName();
                    case XmlPullParser.END_TAG:
                        if(tagName.equals("Valute"))
                        {
                            if(CurrencyName != null && Value != null)
                            {
                                CurencyList.add(new MyCurrencyClass(CurrencyName, Value));
                                CurrencyName = tempValue = null;
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if( tagName.equals("Name")) {
                           if (Objects.equals(xpp.getText(), "Доллар США") | xpp.getText().equals("Евро"))
                           {
                                CurrencyName = xpp.getText();
                            }
                           else {
                                break;
                            }
                        }
                        else if(tagName.equals("Value"))
                        {
                            tempValue = xpp.getText();
                            Value = Float.valueOf(tempValue.replace(',','.'));
                        }
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        }
        catch (XmlPullParserException | IOException e)
        {
            e.printStackTrace();
        }
    }

    XmlPullParser prepareXpp() throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(XMLString));
        return xpp;
    }

    public List<MyCurrencyClass> getCurencyList() {
        return CurencyList;
    }
}