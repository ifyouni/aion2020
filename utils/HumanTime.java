/*     */ package com.aionemu.gameserver.utils;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class HumanTime
/*     */   implements Externalizable, Comparable<HumanTime>, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 5179328390732826722L;
/*     */   private static final long SECOND = 1000L;
/*     */   private static final long MINUTE = 60000L;
/*     */   private static final long HOUR = 3600000L;
/*     */   private static final long DAY = 86400000L;
/*     */   private static final long YEAR = 31536000000L;
/*     */   private static final int CEILING_PERCENTAGE = 15;
/*     */   private long delta;
/*     */ 
/*     */   static State getState(char c)
/*     */   {
/*     */     State out;
/*  25 */     switch (c)
/*     */     {
/*     */     case '0':
/*     */     case '1':
/*     */     case '2':
/*     */     case '3':
/*     */     case '4':
/*     */     case '5':
/*     */     case '6':
/*     */     case '7':
/*     */     case '8':
/*     */     case '9':
/*  36 */       out = State.NUMBER;
/*  37 */       break;
/*     */     case 'D':
/*     */     case 'H':
/*     */     case 'M':
/*     */     case 'S':
/*     */     case 'Y':
/*     */     case 'd':
/*     */     case 'h':
/*     */     case 'm':
/*     */     case 's':
/*     */     case 'y':
/*  48 */       out = State.UNIT;
/*  49 */       break;
/*     */     case ':':
/*     */     case ';':
/*     */     case '<':
/*     */     case '=':
/*     */     case '>':
/*     */     case '?':
/*     */     case '@':
/*     */     case 'A':
/*     */     case 'B':
/*     */     case 'C':
/*     */     case 'E':
/*     */     case 'F':
/*     */     case 'G':
/*     */     case 'I':
/*     */     case 'J':
/*     */     case 'K':
/*     */     case 'L':
/*     */     case 'N':
/*     */     case 'O':
/*     */     case 'P':
/*     */     case 'Q':
/*     */     case 'R':
/*     */     case 'T':
/*     */     case 'U':
/*     */     case 'V':
/*     */     case 'W':
/*     */     case 'X':
/*     */     case 'Z':
/*     */     case '[':
/*     */     case '\\':
/*     */     case ']':
/*     */     case '^':
/*     */     case '_':
/*     */     case '`':
/*     */     case 'a':
/*     */     case 'b':
/*     */     case 'c':
/*     */     case 'e':
/*     */     case 'f':
/*     */     case 'g':
/*     */     case 'i':
/*     */     case 'j':
/*     */     case 'k':
/*     */     case 'l':
/*     */     case 'n':
/*     */     case 'o':
/*     */     case 'p':
/*     */     case 'q':
/*     */     case 'r':
/*     */     case 't':
/*     */     case 'u':
/*     */     case 'v':
/*     */     case 'w':
/*     */     case 'x':
/*     */     default:
/*  51 */       out = State.IGNORED;
/*     */     }
/*  53 */     return out;
/*     */   }
/*     */ 
/*     */   public static HumanTime eval(CharSequence s) {
/*  57 */     HumanTime out = new HumanTime(0L);
/*  58 */     int num = 0;
/*  59 */     int start = 0;
/*  60 */     int end = 0;
/*  61 */     State oldState = State.IGNORED;
/*  62 */     for (Iterator i$ = new Iterable(s) {
/*     */       public Iterator<Character> iterator() {
/*  64 */         return new Iterator() {
/*     */           private int p;
/*     */ 
/*     */           public boolean hasNext() { return (this.p < HumanTime.1.this.val$s.length()); }
/*     */ 
/*     */           public Character next() {
/*  70 */             return Character.valueOf(HumanTime.1.this.val$s.charAt(this.p++)); }
/*     */ 
/*     */           public void remove() {
/*  73 */             throw new UnsupportedOperationException();
/*     */           }
/*     */         };
/*     */       }
/*     */     }
/*  62 */     .iterator(); i$.hasNext(); ) { char c = ((Character)i$.next()).charValue();
/*     */ 
/*  78 */       State newState = getState(c);
/*  79 */       if (oldState != newState) {
/*  80 */         if ((oldState == State.NUMBER) && (((newState == State.IGNORED) || (newState == State.UNIT)))) {
/*  81 */           num = Integer.parseInt(s.subSequence(start, end).toString());
/*  82 */         } else if ((oldState == State.UNIT) && (((newState == State.IGNORED) || (newState == State.NUMBER)))) {
/*  83 */           out.nTimes(s.subSequence(start, end).toString(), num);
/*  84 */           num = 0;
/*     */         }
/*  86 */         start = end;
/*     */       }
/*  88 */       ++end;
/*  89 */       oldState = newState; }
/*  90 */     if (oldState == State.UNIT) {
/*  91 */       out.nTimes(s.subSequence(start, end).toString(), num);
/*     */     }
/*  93 */     return out;
/*     */   }
/*     */ 
/*     */   public static String exactly(CharSequence in) {
/*  97 */     return eval(in).getExactly();
/*     */   }
/*     */ 
/*     */   public static String exactly(long l) {
/* 101 */     return new HumanTime(l).getExactly();
/*     */   }
/*     */ 
/*     */   public static String approximately(CharSequence in) {
/* 105 */     return eval(in).getApproximately();
/*     */   }
/*     */ 
/*     */   public static String approximately(long l) {
/* 109 */     return new HumanTime(l).getApproximately();
/*     */   }
/*     */ 
/*     */   public HumanTime()
/*     */   {
/* 115 */     this(0L);
/*     */   }
/*     */ 
/*     */   public HumanTime(long delta)
/*     */   {
/* 120 */     this.delta = Math.abs(delta);
/*     */   }
/*     */ 
/*     */   private void nTimes(String unit, int n) {
/* 124 */     if ("ms".equalsIgnoreCase(unit))
/* 125 */       ms(n);
/* 126 */     else if ("s".equalsIgnoreCase(unit))
/* 127 */       s(n);
/* 128 */     else if ("m".equalsIgnoreCase(unit))
/* 129 */       m(n);
/* 130 */     else if ("h".equalsIgnoreCase(unit))
/* 131 */       h(n);
/* 132 */     else if ("d".equalsIgnoreCase(unit))
/* 133 */       d(n);
/* 134 */     else if ("y".equalsIgnoreCase(unit))
/* 135 */       y(n);
/*     */   }
/*     */ 
/*     */   private long upperCeiling(long x)
/*     */   {
/* 140 */     return (x / 100L * 85L);
/*     */   }
/*     */ 
/*     */   private long lowerCeiling(long x) {
/* 144 */     return (x / 100L * 15L);
/*     */   }
/*     */ 
/*     */   private String ceil(long d, long n) {
/* 148 */     return Integer.toString((int)Math.ceil(d / n));
/*     */   }
/*     */ 
/*     */   private String floor(long d, long n) {
/* 152 */     return Integer.toString((int)Math.floor(d / n));
/*     */   }
/*     */ 
/*     */   public HumanTime y() {
/* 156 */     return y(1);
/*     */   }
/*     */ 
/*     */   public HumanTime y(int n) {
/* 160 */     this.delta += 31536000000L * Math.abs(n);
/* 161 */     return this;
/*     */   }
/*     */ 
/*     */   public HumanTime d() {
/* 165 */     return d(1);
/*     */   }
/*     */ 
/*     */   public HumanTime d(int n) {
/* 169 */     this.delta += 86400000L * Math.abs(n);
/* 170 */     return this;
/*     */   }
/*     */ 
/*     */   public HumanTime h() {
/* 174 */     return h(1);
/*     */   }
/*     */ 
/*     */   public HumanTime h(int n) {
/* 178 */     this.delta += 3600000L * Math.abs(n);
/* 179 */     return this;
/*     */   }
/*     */ 
/*     */   public HumanTime m() {
/* 183 */     return m(1);
/*     */   }
/*     */ 
/*     */   public HumanTime m(int n) {
/* 187 */     this.delta += 60000L * Math.abs(n);
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */   public HumanTime s() {
/* 192 */     return s(1);
/*     */   }
/*     */ 
/*     */   public HumanTime s(int n) {
/* 196 */     this.delta += 1000L * Math.abs(n);
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */   public HumanTime ms() {
/* 201 */     return ms(1);
/*     */   }
/*     */ 
/*     */   public HumanTime ms(int n) {
/* 205 */     this.delta += Math.abs(n);
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */   public String getExactly() {
/* 210 */     return ((StringBuilder)getExactly(new StringBuilder())).toString();
/*     */   }
/*     */ 
/*     */   public <T extends Appendable> T getExactly(T a) {
/*     */     try {
/* 215 */       boolean prependBlank = false;
/* 216 */       long d = this.delta;
/* 217 */       if (d >= 31536000000L) {
/* 218 */         a.append(floor(d, 31536000000L));
/* 219 */         a.append(' ');
/* 220 */         a.append('y');
/* 221 */         prependBlank = true;
/*     */       }
/* 223 */       d %= 31536000000L;
/* 224 */       if (d >= 86400000L) {
/* 225 */         if (prependBlank) {
/* 226 */           a.append(' ');
/*     */         }
/* 228 */         a.append(floor(d, 86400000L));
/* 229 */         a.append(' ');
/* 230 */         a.append('d');
/* 231 */         prependBlank = true;
/*     */       }
/* 233 */       d %= 86400000L;
/* 234 */       if (d >= 3600000L) {
/* 235 */         if (prependBlank) {
/* 236 */           a.append(' ');
/*     */         }
/* 238 */         a.append(floor(d, 3600000L));
/* 239 */         a.append(' ');
/* 240 */         a.append('h');
/* 241 */         prependBlank = true;
/*     */       }
/* 243 */       d %= 3600000L;
/* 244 */       if (d >= 60000L) {
/* 245 */         if (prependBlank) {
/* 246 */           a.append(' ');
/*     */         }
/* 248 */         a.append(floor(d, 60000L));
/* 249 */         a.append(' ');
/* 250 */         a.append('m');
/* 251 */         prependBlank = true;
/*     */       }
/* 253 */       d %= 60000L;
/* 254 */       if (d >= 1000L) {
/* 255 */         if (prependBlank) {
/* 256 */           a.append(' ');
/*     */         }
/* 258 */         a.append(floor(d, 1000L));
/* 259 */         a.append(' ');
/* 260 */         a.append('s');
/* 261 */         prependBlank = true;
/*     */       }
/* 263 */       d %= 1000L;
/* 264 */       if (d > 0L) {
/* 265 */         if (prependBlank) {
/* 266 */           a.append(' ');
/*     */         }
/* 268 */         a.append(Integer.toString((int)d));
/* 269 */         a.append(' ');
/* 270 */         a.append('m');
/* 271 */         a.append('s');
/*     */       }
/*     */     } catch (IOException localIOException) {
/*     */     }
/* 275 */     return a;
/*     */   }
/*     */ 
/*     */   public String getApproximately() {
/* 279 */     return ((StringBuilder)getApproximately(new StringBuilder())).toString();
/*     */   }
/*     */ 
/*     */   public <T extends Appendable> T getApproximately(T a) {
/*     */     try {
/* 284 */       int parts = 0;
/* 285 */       boolean rounded = false;
/* 286 */       boolean prependBlank = false;
/* 287 */       long d = this.delta;
/* 288 */       long mod = d % 31536000000L;
/* 289 */       if (mod >= upperCeiling(31536000000L)) {
/* 290 */         a.append(ceil(d, 31536000000L));
/* 291 */         a.append(' ');
/* 292 */         a.append('y');
/* 293 */         ++parts;
/* 294 */         rounded = true;
/* 295 */         prependBlank = true;
/* 296 */       } else if (d >= 31536000000L) {
/* 297 */         a.append(floor(d, 31536000000L));
/* 298 */         a.append(' ');
/* 299 */         a.append('y');
/* 300 */         ++parts;
/* 301 */         rounded = mod <= lowerCeiling(31536000000L);
/* 302 */         prependBlank = true; }
/* 303 */       if (!(rounded)) {
/* 304 */         d %= 31536000000L;
/* 305 */         mod = d % 86400000L;
/* 306 */         if (mod >= upperCeiling(86400000L)) {
/* 307 */           if (prependBlank) {
/* 308 */             a.append(' ');
/*     */           }
/* 310 */           a.append(ceil(d, 86400000L));
/* 311 */           a.append(' ');
/* 312 */           a.append('d');
/* 313 */           ++parts;
/* 314 */           rounded = true;
/* 315 */           prependBlank = true;
/* 316 */         } else if (d >= 86400000L) {
/* 317 */           if (prependBlank) {
/* 318 */             a.append(' ');
/*     */           }
/* 320 */           a.append(floor(d, 86400000L));
/* 321 */           a.append(' ');
/* 322 */           a.append('d');
/* 323 */           ++parts;
/* 324 */           rounded = mod <= lowerCeiling(86400000L);
/* 325 */           prependBlank = true; }
/* 326 */         if (parts < 2) {
/* 327 */           d %= 86400000L;
/* 328 */           mod = d % 3600000L;
/* 329 */           if (mod >= upperCeiling(3600000L)) {
/* 330 */             if (prependBlank) {
/* 331 */               a.append(' ');
/*     */             }
/* 333 */             a.append(ceil(d, 3600000L));
/* 334 */             a.append(' ');
/* 335 */             a.append('h');
/* 336 */             ++parts;
/* 337 */             rounded = true;
/* 338 */             prependBlank = true;
/* 339 */           } else if ((d >= 3600000L) && (!(rounded))) {
/* 340 */             if (prependBlank) {
/* 341 */               a.append(' ');
/*     */             }
/* 343 */             a.append(floor(d, 3600000L));
/* 344 */             a.append(' ');
/* 345 */             a.append('h');
/* 346 */             ++parts;
/* 347 */             rounded = mod <= lowerCeiling(3600000L);
/* 348 */             prependBlank = true; }
/* 349 */           if (parts < 2) {
/* 350 */             d %= 3600000L;
/* 351 */             mod = d % 60000L;
/* 352 */             if (mod >= upperCeiling(60000L)) {
/* 353 */               if (prependBlank) {
/* 354 */                 a.append(' ');
/*     */               }
/* 356 */               a.append(ceil(d, 60000L));
/* 357 */               a.append(' ');
/* 358 */               a.append('m');
/* 359 */               ++parts;
/* 360 */               rounded = true;
/* 361 */               prependBlank = true;
/* 362 */             } else if ((d >= 60000L) && (!(rounded))) {
/* 363 */               if (prependBlank) {
/* 364 */                 a.append(' ');
/*     */               }
/* 366 */               a.append(floor(d, 60000L));
/* 367 */               a.append(' ');
/* 368 */               a.append('m');
/* 369 */               ++parts;
/* 370 */               rounded = mod <= lowerCeiling(60000L);
/* 371 */               prependBlank = true; }
/* 372 */             if (parts < 2) {
/* 373 */               d %= 60000L;
/* 374 */               mod = d % 1000L;
/* 375 */               if (mod >= upperCeiling(1000L)) {
/* 376 */                 if (prependBlank) {
/* 377 */                   a.append(' ');
/*     */                 }
/* 379 */                 a.append(ceil(d, 1000L));
/* 380 */                 a.append(' ');
/* 381 */                 a.append('s');
/* 382 */                 ++parts;
/* 383 */                 rounded = true;
/* 384 */                 prependBlank = true;
/* 385 */               } else if ((d >= 1000L) && (!(rounded))) {
/* 386 */                 if (prependBlank) {
/* 387 */                   a.append(' ');
/*     */                 }
/* 389 */                 a.append(floor(d, 1000L));
/* 390 */                 a.append(' ');
/* 391 */                 a.append('s');
/* 392 */                 ++parts;
/* 393 */                 rounded = mod <= lowerCeiling(1000L);
/* 394 */                 prependBlank = true; }
/* 395 */               if (parts < 2) {
/* 396 */                 d %= 1000L;
/*     */ 
/* 398 */                 if ((d > 0L) && (!(rounded))) {
/* 399 */                   if (prependBlank) {
/* 400 */                     a.append(' ');
/*     */                   }
/* 402 */                   a.append(Integer.toString((int)d));
/* 403 */                   a.append(' ');
/* 404 */                   a.append('m');
/* 405 */                   a.append('s');
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (IOException localIOException) {
/*     */     }
/* 414 */     return a;
/*     */   }
/*     */ 
/*     */   public long getDelta() {
/* 418 */     return this.delta;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 423 */     if (this == obj)
/* 424 */       return true;
/* 425 */     if (!(obj instanceof HumanTime)) {
/* 426 */       return false;
/*     */     }
/* 428 */     return (this.delta == ((HumanTime)obj).delta);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 433 */     return (int)(this.delta ^ this.delta >> 32);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 438 */     return getExactly();
/*     */   }
/*     */ 
/*     */   public int compareTo(HumanTime t) {
/* 442 */     return ((this.delta < t.delta) ? -1 : (this.delta == t.delta) ? 0 : 1);
/*     */   }
/*     */ 
/*     */   public Object clone() throws CloneNotSupportedException
/*     */   {
/* 447 */     return super.clone();
/*     */   }
/*     */ 
/*     */   public void readExternal(ObjectInput in) throws IOException {
/* 451 */     this.delta = in.readLong();
/*     */   }
/*     */ 
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/* 455 */     out.writeLong(this.delta);
/*     */   }
/*     */ 
/*     */   static enum State
/*     */   {
/*  19 */     NUMBER, IGNORED, UNIT;
/*     */   }
/*     */ }