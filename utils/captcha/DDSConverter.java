/*     */ package com.aionemu.gameserver.utils.captcha;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ public class DDSConverter
/*     */ {
/*     */   private static final int DDSD_CAPS = 1;
/*     */   private static final int DDSD_HEIGHT = 2;
/*     */   private static final int DDSD_WIDTH = 4;
/*     */   private static final int DDSD_PIXELFORMAT = 4096;
/*     */   private static final int DDSD_MIPMAPCOUNT = 131072;
/*     */   private static final int DDSD_LINEARSIZE = 524288;
/*     */   private static final int DDPF_FOURCC = 4;
/*     */   private static final int DDSCAPS_TEXTURE = 4096;
/*     */ 
/*     */   public static ByteBuffer convertToDxt1NoTransparency(BufferedImage image)
/*     */   {
/*  78 */     if (image == null) {
/*  79 */       return null;
/*     */     }
/*     */ 
/*  82 */     int[] pixels = new int[16];
/*  83 */     int bufferSize = 128 + image.getWidth() * image.getHeight() / 2;
/*  84 */     ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
/*  85 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/*  86 */     buildHeaderDxt1(buffer, image.getWidth(), image.getHeight());
/*     */ 
/*  88 */     int numTilesWide = image.getWidth() / 4;
/*  89 */     int numTilesHigh = image.getHeight() / 4;
/*  90 */     for (int i = 0; i < numTilesHigh; ++i) {
/*  91 */       for (int j = 0; j < numTilesWide; ++j) {
/*  92 */         BufferedImage originalTile = image.getSubimage(j * 4, i * 4, 4, 4);
/*  93 */         originalTile.getRGB(0, 0, 4, 4, pixels, 0, 4);
/*  94 */         Color[] colors = getColors888(pixels);
/*     */ 
/*  96 */         for (int k = 0; k < pixels.length; ++k) {
/*  97 */           pixels[k] = getPixel565(colors[k]);
/*  98 */           colors[k] = getColor565(pixels[k]);
/*     */         }
/*     */ 
/* 101 */         int[] extremaIndices = determineExtremeColors(colors);
/* 102 */         if (pixels[extremaIndices[0]] < pixels[extremaIndices[1]]) {
/* 103 */           int t = extremaIndices[0];
/* 104 */           extremaIndices[0] = extremaIndices[1];
/* 105 */           extremaIndices[1] = t;
/*     */         }
/*     */ 
/* 108 */         buffer.putShort((short)pixels[extremaIndices[0]]);
/* 109 */         buffer.putShort((short)pixels[extremaIndices[1]]);
/*     */ 
/* 111 */         long bitmask = computeBitMask(colors, extremaIndices);
/* 112 */         buffer.putInt((int)bitmask);
/*     */       }
/*     */     }
/*     */ 
/* 116 */     return buffer;
/*     */   }
/*     */ 
/*     */   protected static void buildHeaderDxt1(ByteBuffer buffer, int width, int height) {
/* 120 */     buffer.rewind();
/* 121 */     buffer.put(68);
/* 122 */     buffer.put(68);
/* 123 */     buffer.put(83);
/* 124 */     buffer.put(32);
/* 125 */     buffer.putInt(124);
/* 126 */     int flag = 659463;
/* 127 */     buffer.putInt(flag);
/* 128 */     buffer.putInt(height);
/* 129 */     buffer.putInt(width);
/* 130 */     buffer.putInt(width * height / 2);
/* 131 */     buffer.putInt(0);
/* 132 */     buffer.putInt(0);
/* 133 */     buffer.position(buffer.position() + 44);
/* 134 */     buffer.putInt(32);
/* 135 */     buffer.putInt(4);
/* 136 */     buffer.put(68);
/* 137 */     buffer.put(88);
/* 138 */     buffer.put(84);
/* 139 */     buffer.put(49);
/* 140 */     buffer.putInt(0);
/* 141 */     buffer.putInt(0);
/* 142 */     buffer.putInt(0);
/* 143 */     buffer.putInt(0);
/* 144 */     buffer.putInt(0);
/* 145 */     buffer.putInt(4096);
/* 146 */     buffer.putInt(0);
/* 147 */     buffer.position(buffer.position() + 12);
/*     */   }
/*     */ 
/*     */   protected static int[] determineExtremeColors(Color[] colors) {
/* 151 */     int farthest = -2147483648;
/* 152 */     int[] ex = new int[2];
/*     */ 
/* 154 */     for (int i = 0; i < colors.length - 1; ++i) {
/* 155 */       for (int j = i + 1; j < colors.length; ++j) {
/* 156 */         int d = distance(colors[i], colors[j]);
/* 157 */         if (d > farthest) {
/* 158 */           farthest = d;
/* 159 */           ex[0] = i;
/* 160 */           ex[1] = j;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 165 */     return ex;
/*     */   }
/*     */ 
/*     */   protected static long computeBitMask(Color[] colors, int[] extremaIndices) {
/* 169 */     Color[] colorPoints = { null, null, new Color(), new Color() };
/* 170 */     colorPoints[0] = colors[extremaIndices[0]];
/* 171 */     colorPoints[1] = colors[extremaIndices[1]];
/* 172 */     if (colorPoints[0].equals(colorPoints[1])) {
/* 173 */       return 0L;
/*     */     }
/* 175 */     Color.access$002(colorPoints[2], (2 * colorPoints[0].r + colorPoints[1].r + 1) / 3);
/* 176 */     Color.access$102(colorPoints[2], (2 * colorPoints[0].g + colorPoints[1].g + 1) / 3);
/* 177 */     Color.access$202(colorPoints[2], (2 * colorPoints[0].b + colorPoints[1].b + 1) / 3);
/* 178 */     Color.access$002(colorPoints[3], (colorPoints[0].r + 2 * colorPoints[1].r + 1) / 3);
/* 179 */     Color.access$102(colorPoints[3], (colorPoints[0].g + 2 * colorPoints[1].g + 1) / 3);
/* 180 */     Color.access$202(colorPoints[3], (colorPoints[0].b + 2 * colorPoints[1].b + 1) / 3);
/*     */ 
/* 182 */     long bitmask = 0L;
/* 183 */     for (int i = 0; i < colors.length; ++i) {
/* 184 */       int closest = 2147483647;
/* 185 */       int mask = 0;
/* 186 */       for (int j = 0; j < colorPoints.length; ++j) {
/* 187 */         int d = distance(colors[i], colorPoints[j]);
/* 188 */         if (d < closest) {
/* 189 */           closest = d;
/* 190 */           mask = j;
/*     */         }
/*     */       }
/* 193 */       bitmask |= mask << i * 2;
/*     */     }
/*     */ 
/* 196 */     return bitmask;
/*     */   }
/*     */ 
/*     */   protected static int getPixel565(Color color) {
/* 200 */     int r = color.r >> 3;
/* 201 */     int g = color.g >> 2;
/* 202 */     int b = color.b >> 3;
/* 203 */     return (r << 11 | g << 5 | b);
/*     */   }
/*     */ 
/*     */   protected static Color getColor565(int pixel) {
/* 207 */     Color color = new Color();
/*     */ 
/* 209 */     Color.access$002(color, (int)(pixel & 0xF800) >> 11);
/* 210 */     Color.access$102(color, (int)(pixel & 0x7E0) >> 5);
/* 211 */     Color.access$202(color, (int)(pixel & 0x1F));
/*     */ 
/* 213 */     return color;
/*     */   }
/*     */ 
/*     */   protected static Color[] getColors888(int[] pixels) {
/* 217 */     Color[] colors = new Color[pixels.length];
/*     */ 
/* 219 */     for (int i = 0; i < pixels.length; ++i) {
/* 220 */       colors[i] = new Color();
/* 221 */       Color.access$002(colors[i], (int)(pixels[i] & 0xFF0000) >> 16);
/* 222 */       Color.access$102(colors[i], (int)(pixels[i] & 0xFF00) >> 8);
/* 223 */       Color.access$202(colors[i], (int)(pixels[i] & 0xFF));
/*     */     }
/*     */ 
/* 226 */     return colors;
/*     */   }
/*     */ 
/*     */   protected static int distance(Color ca, Color cb) {
/* 230 */     return ((cb.r - ca.r) * (cb.r - ca.r) + (cb.g - ca.g) * (cb.g - ca.g) + (cb.b - ca.b) * (cb.b - ca.b));
/*     */   }
/*     */ 
/*     */   protected static class Color
/*     */   {
/*     */     private int r;
/*     */     private int g;
/*     */     private int b;
/*     */ 
/*     */     public Color()
/*     */     {
/*  38 */       this.r = (this.g = this.b = 0);
/*     */     }
/*     */ 
/*     */     public Color(int r, int g, int b) {
/*  42 */       this.r = r;
/*  43 */       this.g = g;
/*  44 */       this.b = b;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object o)
/*     */     {
/*  49 */       if (this == o)
/*  50 */         return true;
/*  51 */       if ((o == null) || (super.getClass() != o.getClass())) {
/*  52 */         return false;
/*     */       }
/*  54 */       Color color = (Color)o;
/*     */ 
/*  56 */       if (this.b != color.b)
/*  57 */         return false;
/*  58 */       if (this.g != color.g) {
/*  59 */         return false;
/*     */       }
/*     */ 
/*  62 */       return (this.r != color.r);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/*  70 */       int result = this.r;
/*  71 */       result = 29 * result + this.g;
/*  72 */       result = 29 * result + this.b;
/*  73 */       return result;
/*     */     }
/*     */   }
/*     */ }