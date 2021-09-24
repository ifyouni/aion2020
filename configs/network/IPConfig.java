/*    */ package com.aionemu.gameserver.configs.network;
/*    */ 
/*    */ import com.aionemu.commons.network.IPRange;
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.parsers.SAXParser;
/*    */ import javax.xml.parsers.SAXParserFactory;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.helpers.DefaultHandler;
/*    */ 
/*    */ public class IPConfig
/*    */ {
/* 28 */   private static final Logger log = LoggerFactory.getLogger(IPConfig.class);
/*    */   private static final String CONFIG_FILE = "./config/network/ipconfig.xml";
/* 36 */   private static final List<IPRange> ranges = new ArrayList();
/*    */   private static byte[] defaultAddress;
/*    */ 
/*    */   public static void load()
/*    */   {
/*    */     try
/*    */     {
/* 47 */       SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
/* 48 */       parser.parse(new File("./config/network/ipconfig.xml"), new DefaultHandler()
/*    */       {
/*    */         public void startElement(String uri, String localName, String qName, Attributes attributes)
/*    */           throws SAXException
/*    */         {
/* 53 */           if (qName.equals("ipconfig")) {
/*    */             try {
/* 55 */               IPConfig.access$002(InetAddress.getByName(attributes.getValue("default")).getAddress());
/* 56 */               System.out.print("IP = " + IPConfig.getDefaultAddress());
/*    */             }
/*    */             catch (UnknownHostException e) {
/* 59 */               throw new RuntimeException("Failed to resolve DSN for address: " + attributes.getValue("default"), e);
/*    */             }
/*    */           }
/* 62 */           if (qName.equals("iprange")) {
/* 63 */             String min = attributes.getValue("min");
/* 64 */             String max = attributes.getValue("max");
/* 65 */             String address = attributes.getValue("address");
/* 66 */             IPRange ipRange = new IPRange(min, max, address);
/* 67 */             IPConfig.ranges.add(ipRange);
/*    */           }
/*    */         }
/*    */       });
/*    */     }
/*    */     catch (Exception e) {
/* 73 */       log.error("Critical error while parsing ipConfig", e);
/* 74 */       throw new Error("Can't load ipConfig", e);
/*    */     }
/*    */   }
/*    */ 
/*    */   public static List<IPRange> getRanges()
/*    */   {
/* 84 */     return ranges;
/*    */   }
/*    */ 
/*    */   public static byte[] getDefaultAddress()
/*    */   {
/* 93 */     return defaultAddress;
/*    */   }
/*    */ }