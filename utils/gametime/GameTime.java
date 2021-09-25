/*     */ package com.aionemu.gameserver.utils.gametime;
/*     */ 
/*     */ import com.aionemu.gameserver.services.WeatherService;
/*     */ import com.aionemu.gameserver.spawnengine.TemporarySpawnEngine;
/*     */ import java.security.InvalidParameterException;
/*     */ 
/*     */ public class GameTime
/*     */   implements Cloneable
/*     */ {
/*     */   private static final int MINUTES_IN_HOUR = 60;
/*     */   private static final int MINUTES_IN_DAY = 1440;
/*     */   private static final int MINUTES_IN_YEAR = 525600;
/*  30 */   private int gameTime = 0;
/*     */   private DayTime dayTime;
/*     */ 
/*     */   public GameTime(int time)
/*     */   {
/*  65 */     if (time < 0) {
/*  66 */       throw new InvalidParameterException("Time must be >= 0");
/*     */     }
/*  68 */     this.gameTime = time;
/*  69 */     calculateDayTime();
/*     */   }
/*     */ 
/*     */   public int getProperMinutesInMonth(Monthes m)
/*     */   {
/*  79 */     return (m.getDays() * 1440);
/*     */   }
/*     */ 
/*     */   public int getTime()
/*     */   {
/*  88 */     return this.gameTime;
/*     */   }
/*     */ 
/*     */   public void increase()
/*     */   {
/*  95 */     this.gameTime += 1;
/*  96 */     if (getMinute() == 0)
/*  97 */       checkDayTimeChange();
/*     */   }
/*     */ 
/*     */   public void checkDayTimeChange()
/*     */   {
/* 105 */     DayTime oldDayTime = this.dayTime;
/* 106 */     calculateDayTime();
/* 107 */     onHourChange();
/* 108 */     if (oldDayTime != this.dayTime)
/* 109 */       onDayTimeChange();
/*     */   }
/*     */ 
/*     */   public void calculateDayTime()
/*     */   {
/* 117 */     int hour = getHour();
/* 118 */     if ((hour > 21) || (hour < 4))
/* 119 */       this.dayTime = DayTime.NIGHT;
/* 120 */     else if (hour > 16)
/* 121 */       this.dayTime = DayTime.EVENING;
/* 122 */     else if (hour > 8)
/* 123 */       this.dayTime = DayTime.AFTERNOON;
/*     */     else
/* 125 */       this.dayTime = DayTime.MORNING;
/*     */   }
/*     */ 
/*     */   private void onHourChange()
/*     */   {
/* 130 */     TemporarySpawnEngine.onHourChange();
/*     */   }
/*     */ 
/*     */   private void onDayTimeChange()
/*     */   {
/* 137 */     WeatherService.getInstance().checkWeathersTime();
/*     */   }
/*     */ 
/*     */   public int getYear()
/*     */   {
/* 146 */     return (this.gameTime / 525600);
/*     */   }
/*     */ 
/*     */   public int getMonth()
/*     */   {
/* 155 */     int answer = 1;
/* 156 */     int minutesInYear = this.gameTime % 525600;
/* 157 */     for (Monthes m : Monthes.values()) {
/* 158 */       if (minutesInYear - getProperMinutesInMonth(m) > 0) {
/* 159 */         minutesInYear -= getProperMinutesInMonth(m);
/* 160 */         answer += 1; } else {
/* 161 */         if (minutesInYear - getProperMinutesInMonth(m) != 0) break;
/* 162 */         answer += 1;
/* 163 */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 168 */     return answer;
/*     */   }
/*     */ 
/*     */   public int getDay()
/*     */   {
/* 177 */     int answer = 1;
/* 178 */     int minutesInYear = this.gameTime % 525600;
/* 179 */     for (Monthes m : Monthes.values()) {
/* 180 */       if (minutesInYear - getProperMinutesInMonth(m) > 0) {
/* 181 */         minutesInYear -= getProperMinutesInMonth(m); } else {
/* 182 */         if (minutesInYear - getProperMinutesInMonth(m) == 0) {
/*     */           break;
/*     */         }
/* 185 */         answer = minutesInYear / 1440 + 1;
/* 186 */         break;
/*     */       }
/*     */     }
/* 189 */     return answer;
/*     */   }
/*     */ 
/*     */   public int getHour()
/*     */   {
/* 198 */     return (this.gameTime % 1440 / 60);
/*     */   }
/*     */ 
/*     */   public int getMinute()
/*     */   {
/* 207 */     return (this.gameTime % 60);
/*     */   }
/*     */ 
/*     */   public DayTime getDayTime()
/*     */   {
/* 214 */     return this.dayTime;
/*     */   }
/*     */ 
/*     */   public int convertTime()
/*     */   {
/* 223 */     return (getTime() / 12);
/*     */   }
/*     */ 
/*     */   public GameTime minus(GameTime gt)
/*     */   {
/* 233 */     return new GameTime(getTime() - gt.getTime());
/*     */   }
/*     */ 
/*     */   public GameTime plus(GameTime gt)
/*     */   {
/* 243 */     return new GameTime(getTime() + gt.getTime());
/*     */   }
/*     */ 
/*     */   public boolean isGreaterThan(GameTime gt)
/*     */   {
/* 253 */     return (getTime() > gt.getTime());
/*     */   }
/*     */ 
/*     */   public boolean isLessThan(GameTime gt)
/*     */   {
/* 263 */     return (getTime() < gt.getTime());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 275 */     GameTime other = (GameTime)o;
/* 276 */     return (getTime() == other.getTime());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 282 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 287 */     return new GameTime(this.gameTime);
/*     */   }
/*     */ 
/*     */   private static enum Monthes
/*     */   {
/*  33 */     JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER;
/*     */ 
/*     */     private int _days;
/*     */ 
/*     */     public int getDays()
/*     */     {
/*  55 */       return this._days;
/*     */     }
/*     */   }
/*     */ }