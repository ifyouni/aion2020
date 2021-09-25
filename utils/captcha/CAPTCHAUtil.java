/*     */ package com.aionemu.gameserver.utils.captcha;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ public class CAPTCHAUtil
/*     */ {
/*     */   private static final int DEFAULT_WORD_LENGTH = 6;
/*     */   private static final String WORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
/*     */   private static final int IMAGE_WIDTH = 160;
/*     */   private static final int IMAGE_HEIGHT = 80;
/*     */   private static final int TEXT_SIZE = 25;
/*     */   private static final String FONT_FAMILY_NAME = "Verdana";
/*     */ 
/*     */   public static ByteBuffer createCAPTCHA(String word)
/*     */   {
/*  39 */     ByteBuffer byteBuffer = null;
/*  40 */     BufferedImage bImg = createImage(word);
/*     */ 
/*  42 */     byteBuffer = DDSConverter.convertToDxt1NoTransparency(bImg);
/*     */ 
/*  44 */     return byteBuffer;
/*     */   }
/*     */ 
/*     */   private static BufferedImage createImage(String word)
/*     */   {
/*  55 */     BufferedImage bImg = null;
/*     */     try
/*     */     {
/*  59 */       bImg = new BufferedImage(160, 80, 3);
/*  60 */       Graphics2D g2 = bImg.createGraphics();
/*     */ 
/*  63 */       g2.setColor(Color.BLACK);
/*  64 */       g2.fillRect(0, 0, 160, 80);
/*     */ 
/*  67 */       Font font = new Font("Verdana", 1, 25);
/*  68 */       g2.setFont(font);
/*  69 */       g2.setColor(Color.WHITE);
/*  70 */       g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/*     */ 
/*  73 */       char[] chars = word.toCharArray();
/*  74 */       int x = 10;
/*  75 */       int y = 52;
/*     */ 
/*  77 */       for (int i = 0; i < chars.length; ++i) {
/*  78 */         char ch = chars[i];
/*  79 */         g2.drawString(String.valueOf(ch), x + font.getSize() * i, y + (int)Math.pow(-1.0D, i) * 4);
/*     */       }
/*     */ 
/*  83 */       g2.dispose();
/*     */     }
/*     */     catch (Exception e) {
/*  86 */       e.printStackTrace();
/*  87 */       bImg = null;
/*     */     }
/*     */ 
/*  90 */     return bImg;
/*     */   }
/*     */ 
/*     */   public static String getRandomWord()
/*     */   {
/*  97 */     return randomWord(6);
/*     */   }
/*     */ 
/*     */   private static String randomWord(int wordLength)
/*     */   {
/* 104 */     StringBuffer word = new StringBuffer();
/*     */ 
/* 106 */     for (int i = 0; i < wordLength; ++i) {
/* 107 */       int index = Math.abs((int)(Math.random() * "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789".length()));
/* 108 */       char ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789".charAt(index);
/* 109 */       word.append(ch);
/*     */     }
/*     */ 
/* 112 */     return word.toString();
/*     */   }
/*     */ }