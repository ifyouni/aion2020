/*    */ package com.aionemu.gameserver.utils;
/*    */ 
/*    */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*    */ import com.aionemu.gameserver.model.templates.BoundRadius;
/*    */ import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
/*    */ 
/*    */ public class PositionUtil
/*    */ {
/*    */   private static final float MAX_ANGLE_DIFF = 90.0F;
/*    */ 
/*    */   public static boolean isBehindTarget(VisibleObject object1, VisibleObject object2)
/*    */   {
/* 10 */     float angleObject1 = MathUtil.calculateAngleFrom(object1, object2);
/* 11 */     float angleObject2 = MathUtil.convertHeadingToDegree(object2.getHeading());
/* 12 */     float angleDiff = angleObject1 - angleObject2;
/* 13 */     if (angleDiff <= -270.0F)
/* 14 */       angleDiff += 360.0F;
/* 15 */     if (angleDiff >= 270.0F)
/* 16 */       angleDiff -= 360.0F;
/* 17 */     return (Math.abs(angleDiff) <= 90.0F);
/*    */   }
/*    */ 
/*    */   public static boolean isInFrontOfTarget(VisibleObject object1, VisibleObject object2) {
/* 21 */     float angleObject2 = MathUtil.calculateAngleFrom(object2, object1);
/* 22 */     float angleObject1 = MathUtil.convertHeadingToDegree(object2.getHeading());
/* 23 */     float angleDiff = angleObject1 - angleObject2;
/* 24 */     if (angleDiff <= -270.0F)
/* 25 */       angleDiff += 360.0F;
/* 26 */     if (angleDiff >= 270.0F)
/* 27 */       angleDiff -= 360.0F;
/* 28 */     return (Math.abs(angleDiff) <= 90.0F);
/*    */   }
/*    */ 
/*    */   public static boolean isBehind(VisibleObject object1, VisibleObject object2) {
/* 32 */     float angle = MathUtil.convertHeadingToDegree(object1.getHeading()) + 90.0F;
/* 33 */     if (angle >= 360.0F)
/* 34 */       angle -= 360.0F;
/* 35 */     double radian = Math.toRadians(angle);
/* 36 */     float x0 = object1.getX();
/* 37 */     float y0 = object1.getY();
/* 38 */     float x1 = (float)(Math.cos(radian) * 5.0D) + x0;
/* 39 */     float y1 = (float)(Math.sin(radian) * 5.0D) + y0;
/* 40 */     float xA = object2.getX();
/* 41 */     float yA = object2.getY();
/* 42 */     float temp = (x1 - x0) * (yA - y0) - ((y1 - y0) * (xA - x0));
/* 43 */     return (temp > 0.0F);
/*    */   }
/*    */ 
/*    */   public static float getAngleToTarget(VisibleObject object1, VisibleObject object2) {
/* 47 */     float angleObject1 = MathUtil.convertHeadingToDegree(object1.getHeading()) - 180.0F;
/* 48 */     if (angleObject1 < 0.0F)
/* 49 */       angleObject1 += 360.0F;
/* 50 */     float angleObject2 = MathUtil.calculateAngleFrom(object1, object2);
/* 51 */     float angleDiff = angleObject1 - angleObject2 - 180.0F;
/* 52 */     if (angleDiff < 0.0F)
/* 53 */       angleDiff += 360.0F;
/* 54 */     return angleDiff;
/*    */   }
/*    */ 
/*    */   public static float getDirectionalBound(VisibleObject object1, VisibleObject object2, boolean inverseTarget) {
/* 58 */     float angle = 90.0F - ((inverseTarget) ? getAngleToTarget(object2, object1) : getAngleToTarget(object1, object2));
/* 59 */     if (angle < 0.0F)
/* 60 */       angle += 360.0F;
/* 61 */     double radians = Math.toRadians(angle);
/* 62 */     float x1 = (float)(object1.getX() + object1.getObjectTemplate().getBoundRadius().getSide() * Math.cos(radians));
/* 63 */     float y1 = (float)(object1.getY() + object1.getObjectTemplate().getBoundRadius().getFront() * Math.sin(radians));
/* 64 */     float x2 = (float)(object2.getX() + object2.getObjectTemplate().getBoundRadius().getSide() * Math.cos(3.141592653589793D + radians));
/* 65 */     float y2 = (float)(object2.getY() + object2.getObjectTemplate().getBoundRadius().getFront() * Math.sin(3.141592653589793D + radians));
/* 66 */     float bound1 = (float)MathUtil.getDistance(object1.getX(), object1.getY(), x1, y1);
/* 67 */     float bound2 = (float)MathUtil.getDistance(object2.getX(), object2.getY(), x2, y2);
/* 68 */     return (bound1 - bound2);
/*    */   }
/*    */ 
/*    */   public static float getDirectionalBound(VisibleObject object1, VisibleObject object2) {
/* 72 */     return getDirectionalBound(object1, object2, false);
/*    */   }
/*    */ 
/*    */   public static byte getMoveAwayHeading(VisibleObject fromObject, VisibleObject object) {
/* 76 */     float angle = MathUtil.calculateAngleFrom(fromObject, object);
/* 77 */     byte heading = MathUtil.convertDegreeToHeading(angle);
/* 78 */     return heading;
/*    */   }
/*    */ }