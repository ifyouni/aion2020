/*     */ package com.aionemu.gameserver.cache;
/*     */ 
/*     */ import com.aionemu.gameserver.configs.main.HTMLConfig;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javolution.util.FastMap;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public final class HTMLCache
/*     */ {
/*  31 */   private static final Logger log = LoggerFactory.getLogger(HTMLCache.class);
/*     */ 
/*  33 */   private static final FileFilter HTML_FILTER = new FileFilter()
/*     */   {
/*     */     public boolean accept(File file)
/*     */     {
/*  37 */       return ((file.isDirectory()) || (file.getName().endsWith(".xhtml")));
/*     */     }
/*  33 */   };
/*     */ 
/*  41 */   private static final File HTML_ROOT = new File(HTMLConfig.HTML_ROOT);
/*     */ 
/*  52 */   private FastMap<String, String> cache = new FastMap(16000);
/*     */   private int loadedFiles;
/*     */   private int size;
/*     */   private static final String[] TAGS_TO_COMPACT;
/*     */ 
/*     */   public static HTMLCache getInstance() {
/*  49 */     return SingletonHolder.INSTANCE;
/*     */   }
/*     */ 
/*     */   private HTMLCache()
/*     */   {
/*  58 */     reload(false);
/*     */   }
/*     */ 
/*     */   public synchronized void reload(boolean deleteCacheFile)
/*     */   {
/*  63 */     this.cache.clear();
/*  64 */     this.loadedFiles = 0;
/*  65 */     this.size = 0;
/*     */ 
/*  67 */     File cacheFile = getCacheFile();
/*     */ 
/*  69 */     if ((deleteCacheFile) && (cacheFile.exists())) {
/*  70 */       log.info("缓存[HTML]：删除缓存文件... OK.");
/*     */ 
/*  72 */       cacheFile.delete();
/*     */     }
/*     */ 
/*  75 */     log.info("缓存[HTML]：缓存开始... OK.");
/*     */ 
/*  77 */     if (cacheFile.exists()) {
/*  78 */       log.info("缓存[HTML]：使用缓存文件... OK.");
/*     */ 
/*  80 */       ObjectInputStream ois = null;
/*     */       try {
/*  82 */         ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(getCacheFile())));
/*     */ 
/*  84 */         this.cache = ((FastMap)ois.readObject());
/*     */ 
/*  86 */         for (String html : this.cache.values()) {
/*  87 */           this.loadedFiles += 1;
/*  88 */           this.size += html.length();
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/*  92 */         log.warn("", e);
/*     */ 
/*  94 */         reload(true);
/*     */ 
/*  98 */         return; } finally { IOUtils.closeQuietly(ois);
/*     */       }
/*     */     }
/*     */     else {
/* 102 */       parseDir(HTML_ROOT);
/*     */     }
/*     */ 
/* 105 */     log.info(String.valueOf(this));
/*     */ 
/* 107 */     if (cacheFile.exists()) {
/* 108 */       log.info("缓存[HTML]：压缩被跳过!");
/*     */     }
/*     */     else {
/* 111 */       log.info("缓存[HTML]：压缩HTML... OK.");
/*     */ 
/* 113 */       StringBuilder sb = new StringBuilder(8192);
/*     */ 
/* 115 */       for (??? = this.cache.entrySet().iterator(); ???.hasNext(); ) { Map.Entry entry = (Map.Entry)???.next();
/*     */         try {
/* 117 */           String oldHtml = (String)entry.getValue();
/* 118 */           String newHtml = compactHtml(sb, oldHtml);
/*     */ 
/* 120 */           this.size -= oldHtml.length();
/* 121 */           this.size += newHtml.length();
/*     */ 
/* 123 */           entry.setValue(newHtml);
/*     */         }
/*     */         catch (RuntimeException e) {
/* 126 */           log.warn(new StringBuilder().append("缓存[HTML]：压缩时出错 ").append((String)entry.getKey()).toString(), e);
/*     */         }
/*     */       }
/*     */ 
/* 130 */       log.info(String.valueOf(this));
/*     */     }
/*     */ 
/* 133 */     if (!(cacheFile.exists())) {
/* 134 */       log.info("缓存[HTML]：创建缓存文件... OK.");
/*     */ 
/* 136 */       ObjectOutputStream oos = null;
/*     */       try {
/* 138 */         oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(getCacheFile())));
/*     */ 
/* 140 */         oos.writeObject(this.cache);
/*     */       }
/*     */       catch (IOException e) {
/* 143 */         log.warn("", e);
/*     */       }
/*     */       finally {
/* 146 */         IOUtils.closeQuietly(oos);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private File getCacheFile() {
/* 152 */     return new File(HTMLConfig.HTML_CACHE_FILE);
/*     */   }
/*     */ 
/*     */   private String compactHtml(StringBuilder sb, String html)
/*     */   {
/* 182 */     sb.setLength(0);
/* 183 */     sb.append(html);
/*     */ 
/* 185 */     for (int i = 0; i < sb.length(); ++i) {
/* 186 */       if (Character.isWhitespace(sb.charAt(i)))
/* 187 */         sb.setCharAt(i, ' ');
/*     */     }
/* 189 */     replaceAll(sb, "  ", " ");
/*     */ 
/* 191 */     replaceAll(sb, "< ", "<");
/* 192 */     replaceAll(sb, " >", ">");
/*     */ 
/* 194 */     for (i = 0; i < TAGS_TO_COMPACT.length; i += 3) {
/* 195 */       replaceAll(sb, TAGS_TO_COMPACT[(i + 1)], TAGS_TO_COMPACT[i]);
/* 196 */       replaceAll(sb, TAGS_TO_COMPACT[(i + 2)], TAGS_TO_COMPACT[i]);
/*     */     }
/*     */ 
/* 199 */     replaceAll(sb, "  ", " ");
/*     */ 
/* 202 */     int fromIndex = 0;
/* 203 */     int toIndex = sb.length();
/*     */ 
/* 205 */     while ((fromIndex < toIndex) && (sb.charAt(fromIndex) == ' ')) {
/* 206 */       ++fromIndex;
/*     */     }
/* 208 */     while ((fromIndex < toIndex) && (sb.charAt(toIndex - 1) == ' ')) {
/* 209 */       --toIndex;
/*     */     }
/* 211 */     return sb.substring(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */   private void replaceAll(StringBuilder sb, String pattern, String value) {
/* 215 */     for (int index = 0; (index = sb.indexOf(pattern, index)) != -1; )
/* 216 */       sb.replace(index, index + pattern.length(), value);
/*     */   }
/*     */ 
/*     */   public void reloadPath(File f) {
/* 220 */     parseDir(f);
/*     */ 
/* 222 */     log.info("缓存[HTML]：重新加载指定的路径.");
/*     */   }
/*     */ 
/*     */   public void parseDir(File dir) {
/* 226 */     for (File file : dir.listFiles(HTML_FILTER))
/* 227 */       if (!(file.isDirectory()))
/* 228 */         loadFile(file);
/*     */       else
/* 230 */         parseDir(file);
/*     */   }
/*     */ 
/*     */   public String loadFile(File file)
/*     */   {
/* 235 */     if (isLoadable(file)) {
/* 236 */       BufferedInputStream bis = null;
/*     */       try {
/* 238 */         bis = new BufferedInputStream(new FileInputStream(file));
/* 239 */         byte[] raw = new byte[bis.available()];
/* 240 */         bis.read(raw);
/*     */ 
/* 242 */         String content = new String(raw, HTMLConfig.HTML_ENCODING);
/* 243 */         String relpath = getRelativePath(HTML_ROOT, file);
/*     */ 
/* 245 */         this.size += content.length();
/*     */ 
/* 247 */         String oldContent = (String)this.cache.get(relpath);
/* 248 */         if (oldContent == null)
/* 249 */           this.loadedFiles += 1;
/*     */         else {
/* 251 */           this.size -= oldContent.length();
/*     */         }
/* 253 */         this.cache.put(relpath, content);
/*     */ 
/* 255 */         String str1 = content;
/*     */ 
/* 261 */         return str1;
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 258 */         log.warn("Problem with htm file:", e);
/*     */       }
/*     */       finally {
/* 261 */         IOUtils.closeQuietly(bis);
/*     */       }
/*     */     }
/*     */ 
/* 265 */     return null;
/*     */   }
/*     */ 
/*     */   public String getHTML(String path) {
/* 269 */     return ((String)this.cache.get(path));
/*     */   }
/*     */ 
/*     */   private boolean isLoadable(File file) {
/* 273 */     return ((file.exists()) && (!(file.isDirectory())) && (HTML_FILTER.accept(file)));
/*     */   }
/*     */ 
/*     */   public boolean pathExists(String path) {
/* 277 */     return this.cache.containsKey(path);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 282 */     return new StringBuilder().append("缓存[HTML]: ").append(String.format("%.3f", new Object[] { Float.valueOf(this.size / 1024.0F) })).append(" 千字节 ").append(this.loadedFiles).append(" 个文件.").toString();
/*     */   }
/*     */ 
/*     */   public static String getRelativePath(File base, File file) {
/* 286 */     return file.toURI().getPath().substring(base.toURI().getPath().length());
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 159 */     String[] tagsToCompact = { "html", "title", "body", "br", "br1", "p", "table", "tr", "td" };
/*     */ 
/* 161 */     List list = new ArrayList();
/*     */ 
/* 163 */     for (String tag : tagsToCompact) {
/* 164 */       list.add(new StringBuilder().append("<").append(tag).append(">").toString());
/* 165 */       list.add(new StringBuilder().append("</").append(tag).append(">").toString());
/* 166 */       list.add(new StringBuilder().append("<").append(tag).append("/>").toString());
/* 167 */       list.add(new StringBuilder().append("<").append(tag).append(" />").toString());
/*     */     }
/*     */ 
/* 170 */     List list2 = new ArrayList();
/*     */ 
/* 172 */     for (String tag : list) {
/* 173 */       list2.add(tag);
/* 174 */       list2.add(new StringBuilder().append(tag).append(" ").toString());
/* 175 */       list2.add(new StringBuilder().append(" ").append(tag).toString());
/*     */     }
/*     */ 
/* 178 */     TAGS_TO_COMPACT = (String[])list2.toArray(new String[list.size()]);
/*     */   }
/*     */ 
/*     */   private static final class SingletonHolder
/*     */   {
/*  45 */     private static final HTMLCache INSTANCE = new HTMLCache(null);
/*     */   }
/*     */ }