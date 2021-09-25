/*     */ package com.aionemu.gameserver.utils;
/*     */ 
/*     */ import com.aionemu.gameserver.controllers.movement.MoveController;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*     */ import com.aionemu.gameserver.model.geometry.Point3D;
/*     */ import com.aionemu.gameserver.model.templates.BoundRadius;
/*     */ import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
/*     */ import com.aionemu.gameserver.model.templates.zone.Point2D;
/*     */ import java.awt.Point;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.math.MathContext;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class MathUtil
/*     */ {
/*     */   static final BigDecimal TWO;
/*     */   static final double SQRT_10 = 3.16227766016838D;
/*     */ 
/*     */   public static double getDistance(Point2D point1, Point2D point2)
/*     */   {
/*  19 */     return getDistance(point1.getX(), point1.getY(), point2.getX(), point2.getY());
/*     */   }
/*     */ 
/*     */   public static double getDistance(float x1, float y1, float x2, float y2) {
/*  23 */     float dx = x2 - x1;
/*  24 */     float dy = y2 - y1;
/*  25 */     return Math.sqrt(dx * dx + dy * dy);
/*     */   }
/*     */ 
/*     */   public static double getDistance(Point3D point1, Point3D point2) {
/*  29 */     if ((point1 == null) || (point2 == null)) {
/*  30 */       return 0.0D;
/*     */     }
/*  32 */     return getDistance(point1.getX(), point1.getY(), point1.getZ(), point2.getX(), point2.getY(), point2.getZ());
/*     */   }
/*     */ 
/*     */   public static double getDistance(float x1, float y1, float z1, float x2, float y2, float z2) {
/*  36 */     float dx = x1 - x2;
/*  37 */     float dy = y1 - y2;
/*  38 */     float dz = z1 - z2;
/*  39 */     return Math.sqrt(dx * dx + dy * dy + dz * dz);
/*     */   }
/*     */ 
/*     */   public static double getDistance(VisibleObject object, float x, float y, float z) {
/*  43 */     return getDistance(object.getX(), object.getY(), object.getZ(), x, y, z);
/*     */   }
/*     */ 
/*     */   public static double getDistance(VisibleObject object, VisibleObject object2) {
/*  47 */     return getDistance(object.getX(), object.getY(), object.getZ(), object2.getX(), object2.getY(), object2.getZ());
/*     */   }
/*     */ 
/*     */   public static Point2D getClosestPointOnSegment(Point ss, Point se, Point p) {
/*  51 */     return getClosestPointOnSegment(ss.x, ss.y, se.x, se.y, p.x, p.y);
/*     */   }
/*     */ 
/*     */   public static Point2D getClosestPointOnSegment(float sx1, float sy1, float sx2, float sy2, float px, float py)
/*     */   {
/*     */     Point2D closestPoint;
/*  55 */     double xDelta = sx2 - sx1;
/*  56 */     double yDelta = sy2 - sy1;
/*  57 */     if ((xDelta == 0.0D) && (yDelta == 0.0D)) {
/*  58 */       throw new IllegalArgumentException("Segment start equals segment end");
/*     */     }
/*  60 */     double u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);
/*     */ 
/*  62 */     if (u < 0.0D)
/*  63 */       closestPoint = new Point2D(sx1, sy1);
/*  64 */     else if (u > 1.0D)
/*  65 */       closestPoint = new Point2D(sx2, sy2);
/*     */     else {
/*  67 */       closestPoint = new Point2D((float)(sx1 + u * xDelta), (float)(sy1 + u * yDelta));
/*     */     }
/*  69 */     return closestPoint;
/*     */   }
/*     */ 
/*     */   public static double getDistanceToSegment(Point ss, Point se, Point p) {
/*  73 */     return getDistanceToSegment(ss.x, ss.y, se.x, se.y, p.x, p.y);
/*     */   }
/*     */ 
/*     */   public static double getDistanceToSegment(int sx1, int sy1, int sx2, int sy2, int px, int py) {
/*  77 */     Point2D closestPoint = getClosestPointOnSegment(sx1, sy1, sx2, sy2, px, py);
/*  78 */     return getDistance(closestPoint.getX(), closestPoint.getY(), px, py);
/*     */   }
/*     */ 
/*     */   public static boolean isInRange(VisibleObject object1, VisibleObject object2, float range) {
/*  82 */     if ((object1.getWorldId() != object2.getWorldId()) || (object1.getInstanceId() != object2.getInstanceId())) {
/*  83 */       return false;
/*     */     }
/*  85 */     float dx = object2.getX() - object1.getX();
/*  86 */     float dy = object2.getY() - object1.getY();
/*  87 */     return (dx * dx + dy * dy < range * range);
/*     */   }
/*     */ 
/*     */   public static boolean isIn3dRange(VisibleObject object1, VisibleObject object2, float range) {
/*  91 */     if ((object1.getWorldId() != object2.getWorldId()) || (object1.getInstanceId() != object2.getInstanceId())) {
/*  92 */       return false;
/*     */     }
/*  94 */     float dx = object2.getX() - object1.getX();
/*  95 */     float dy = object2.getY() - object1.getY();
/*  96 */     float dz = object2.getZ() - object1.getZ();
/*  97 */     return (dx * dx + dy * dy + dz * dz < range * range);
/*     */   }
/*     */ 
/*     */   public static boolean isIn3dRangeLimited(VisibleObject object1, VisibleObject object2, float minRange, float maxRange) {
/* 101 */     if ((object1.getWorldId() != object2.getWorldId()) || (object1.getInstanceId() != object2.getInstanceId())) {
/* 102 */       return false;
/*     */     }
/* 104 */     float dx = object2.getX() - object1.getX();
/* 105 */     float dy = object2.getY() - object1.getY();
/* 106 */     float dz = object2.getZ() - object1.getZ();
/* 107 */     return ((dx * dx + dy * dy + dz * dz > minRange * minRange) && (dx * dx + dy * dy + dz * dz < maxRange * maxRange));
/*     */   }
/*     */ 
/*     */   public static boolean isIn3dRange(float obj1X, float obj1Y, float obj1Z, float obj2X, float obj2Y, float obj2Z, float range) {
/* 111 */     float dx = obj2X - obj1X;
/* 112 */     float dy = obj2Y - obj1Y;
/* 113 */     float dz = obj2Z - obj1Z;
/* 114 */     return (dx * dx + dy * dy + dz * dz < range * range);
/*     */   }
/*     */ 
/*     */   public static boolean isInSphere(VisibleObject obj, float centerX, float centerY, float centerZ, float radius) {
/* 118 */     float dx = obj.getX() - centerX;
/* 119 */     float dy = obj.getY() - centerY;
/* 120 */     float dz = obj.getZ() - centerZ;
/* 121 */     return (dx * dx + dy * dy + dz * dz < radius * radius);
/*     */   }
/*     */ 
/*     */   public static final float calculateAngleFrom(float obj1X, float obj1Y, float obj2X, float obj2Y) {
/* 125 */     float angleTarget = (float)Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
/* 126 */     if (angleTarget < 0.0F) {
/* 127 */       angleTarget = 360.0F + angleTarget;
/*     */     }
/* 129 */     return angleTarget;
/*     */   }
/*     */ 
/*     */   public static float calculateAngleFrom(VisibleObject obj1, VisibleObject obj2) {
/* 133 */     return calculateAngleFrom(obj1.getX(), obj1.getY(), obj2.getX(), obj2.getY());
/*     */   }
/*     */ 
/*     */   public static final float convertHeadingToDegree(byte clientHeading) {
/* 137 */     float degree = clientHeading * 3;
/* 138 */     return degree;
/*     */   }
/*     */ 
/*     */   public static final byte convertDegreeToHeading(float angle) {
/* 142 */     return (byte)(int)(angle / 3.0F);
/*     */   }
/*     */ 
/*     */   public static final boolean isNearCoordinates(VisibleObject obj, float x, float y, float z, int offset) {
/* 146 */     return (getDistance(obj.getX(), obj.getY(), obj.getZ(), x, y, z) < offset + 0.1F);
/*     */   }
/*     */ 
/*     */   public static final boolean isNearCoordinates(VisibleObject obj, VisibleObject obj2, int offset) {
/* 150 */     return (getDistance(obj.getX(), obj.getY(), obj.getZ(), obj2.getX(), obj2.getY(), obj2.getZ()) < offset + 0.1F);
/*     */   }
/*     */ 
/*     */   public static final boolean isInAttackRange(Creature object1, Creature object2, float range) {
/* 154 */     if ((object1 == null) || (object2 == null))
/* 155 */       return false;
/* 156 */     if ((object1.getWorldId() != object2.getWorldId()) || (object1.getInstanceId() != object2.getInstanceId())) {
/* 157 */       return false;
/*     */     }
/* 159 */     float offset = object1.getObjectTemplate().getBoundRadius().getCollision() + object2.getObjectTemplate().getBoundRadius().getCollision();
/* 160 */     if (object1.getMoveController().isInMove())
/* 161 */       offset = 3.0F;
/* 162 */     if (object2.getMoveController().isInMove()) {
/* 163 */       offset = 3.0F;
/*     */     }
/* 165 */     return (getDistance(object1, object2) - offset <= range);
/*     */   }
/*     */ 
/*     */   public static final boolean isInsideAttackCylinder(VisibleObject obj1, VisibleObject obj2, int length, int radius, boolean isFront) {
/* 169 */     double radian = Math.toRadians(convertHeadingToDegree(obj1.getHeading()));
/* 170 */     int direction = (isFront) ? 0 : 1;
/* 171 */     float dx = (float)(Math.cos(3.141592653589793D * direction + radian) * length);
/* 172 */     float dy = (float)(Math.sin(3.141592653589793D * direction + radian) * length);
/* 173 */     float tdx = obj2.getX() - obj1.getX();
/* 174 */     float tdy = obj2.getY() - obj1.getY();
/* 175 */     float tdz = obj2.getZ() - obj1.getZ();
/* 176 */     float lengthSqr = length * length;
/* 177 */     float dot = tdx * dx + tdy * dy;
/* 178 */     if ((dot < 0.0F) || (dot > lengthSqr)) {
/* 179 */       return false;
/*     */     }
/* 181 */     return (tdx * tdx + tdy * tdy + tdz * tdz - (dot * dot / lengthSqr) <= radius);
/*     */   }
/*     */ 
/*     */   public static final Point get2DPointInsideCircle(float CenterX, float CenterY, int Radius) {
/* 185 */     double X = Math.random() * 2.0D - 1.0D;
/* 186 */     double YMin = -Math.sqrt(1.0D - (X * X));
/* 187 */     double YMax = Math.sqrt(1.0D - (X * X));
/* 188 */     double Y = Math.random() * (YMax - YMin) + YMin;
/* 189 */     double finalX = X * Radius + CenterX;
/* 190 */     double finalY = Y * Radius + CenterY;
/* 191 */     return new Point((int)finalX, (int)finalY);
/*     */   }
/*     */ 
/*     */   public static final Point get2DPointOnCircleCircumference(float CenterX, float CenterY, int Radius, float angleInDegrees) {
/* 195 */     float finalX = (float)(Radius * Math.cos(angleInDegrees * 3.141592653589793D / 180.0D)) + CenterX;
/* 196 */     float finalY = (float)(Radius * Math.sin(angleInDegrees * 3.141592653589793D / 180.0D)) + CenterY;
/* 197 */     return new Point((int)finalX, (int)finalY);
/*     */   }
/*     */ 
/*     */   public static final Point get2DPointOnCircleCircumference(Point CenterPoint, Point EndPoint, int Radius) {
/* 201 */     double AngleinXAxis = getAngle(CenterPoint, EndPoint);
/* 202 */     float finalX = (float)(Radius * Math.cos(AngleinXAxis * 3.141592653589793D / 180.0D)) + CenterPoint.x;
/* 203 */     float finalY = (float)(Radius * Math.sin(AngleinXAxis * 3.141592653589793D / 180.0D)) + CenterPoint.y;
/* 204 */     return new Point((int)finalX, (int)finalY);
/*     */   }
/*     */ 
/*     */   public static final double getAngle(Point P1, Point P2) {
/* 208 */     float dx = P2.x - P1.x;
/* 209 */     float dy = P2.y - P1.y;
/* 210 */     double angle = Math.atan2(dx, dy) * 180.0D / 3.141592653589793D;
/* 211 */     return angle;
/*     */   }
/*     */ 
/*     */   public static final Point get2DPointInsideCircleClosestTo(Point Center, int Radius, Point GivenPoint) {
/* 215 */     double vX = GivenPoint.x - Center.x;
/* 216 */     double vY = GivenPoint.y - Center.y;
/* 217 */     double magV = Math.sqrt(vX * vX + vY * vY);
/* 218 */     double aX = Center.x + vX / magV * Radius;
/* 219 */     double aY = Center.y + vY / magV * Radius;
/* 220 */     return new Point((int)aX, (int)aY);
/*     */   }
/*     */ 
/*     */   public static final Point get2DPointInsideAnnulus(Point Center, int Radius1, int Radius2) {
/* 224 */     double theta = 360.0D * Math.random();
/* 225 */     double dist = Math.sqrt(Math.random() * (Radius1 * Radius1 - (Radius2 * Radius2)) + Radius2 * Radius2);
/* 226 */     double X = dist * Math.cos(theta) + Center.x;
/* 227 */     double Y = dist * Math.sin(theta) + Center.y;
/* 228 */     return new Point((int)X, (int)Y);
/*     */   }
/*     */ 
/*     */   public static boolean isInAnnulus(VisibleObject obj, Point3D Center, float Radius1, float Radius2)
/*     */   {
/* 234 */     return ((isInSphere(obj, Center.getX(), Center.getY(), Center.getZ(), Radius2)) || 
/* 233 */       (!(isInSphere(obj, Center.getX(), Center.getY(), Center.getZ(), Radius1))));
/*     */   }
/*     */ 
/*     */   public static BigDecimal bigSqrt(BigDecimal squarD, MathContext rootMC)
/*     */   {
/* 244 */     int sign = squarD.signum();
/* 245 */     if (sign == -1)
/* 246 */       throw new ArithmeticException("\nSquare root of a negative number: " + squarD);
/* 247 */     if (sign == 0) {
/* 248 */       return squarD.round(rootMC);
/*     */     }
/* 250 */     int prec = rootMC.getPrecision();
/* 251 */     if (prec == 0) {
/* 252 */       throw new IllegalArgumentException("\nMost roots won't have infinite precision = 0");
/*     */     }
/* 254 */     int BITS = 62;
/* 255 */     int nInit = 16;
/* 256 */     MathContext nMC = new MathContext(18, RoundingMode.HALF_DOWN);
/* 257 */     BigDecimal x = null; BigDecimal e = null;
/* 258 */     BigDecimal v = null; BigDecimal g = null;
/* 259 */     BigInteger bi = squarD.unscaledValue();
/* 260 */     int biLen = bi.bitLength();
/* 261 */     int shift = Math.max(0, biLen - BITS + ((biLen % 2 == 0) ? 0 : 1));
/* 262 */     bi = bi.shiftRight(shift);
/* 263 */     double root = Math.sqrt(bi.doubleValue());
/* 264 */     BigDecimal halfBack = new BigDecimal(BigInteger.ONE.shiftLeft(shift / 2));
/* 265 */     int scale = squarD.scale();
/* 266 */     if (scale % 2 == 1) {
/* 267 */       root *= 3.16227766016838D;
/*     */     }
/* 269 */     scale = (int)Math.floor(scale / 2.0D);
/* 270 */     x = new BigDecimal(root, nMC);
/* 271 */     x = x.multiply(halfBack, nMC);
/* 272 */     if (scale != 0)
/* 273 */       x = x.movePointLeft(scale);
/* 274 */     if (prec < nInit) {
/* 275 */       return x.round(rootMC);
/*     */     }
/* 277 */     v = BigDecimal.ONE.divide(TWO.multiply(x), nMC);
/* 278 */     ArrayList nPrecs = new ArrayList();
/* 279 */     if ((!($assertionsDisabled)) && (nInit <= 3)) throw new AssertionError("Never ending loop!");
/* 280 */     for (int m = prec + 1; m > nInit; m = m / 2 + ((m > 100) ? 1 : 2))
/* 281 */       nPrecs.add(Integer.valueOf(m));
/* 282 */     for (int i = nPrecs.size() - 1; i > -1; --i) {
/* 283 */       nMC = new MathContext(((Integer)nPrecs.get(i)).intValue(), (i % 2 == 1) ? RoundingMode.HALF_UP : RoundingMode.HALF_DOWN);
/* 284 */       e = squarD.subtract(x.multiply(x, nMC), nMC);
/* 285 */       if (i != 0) {
/* 286 */         x = x.add(e.multiply(v, nMC));
/*     */       } else {
/* 288 */         x = x.add(e.multiply(v, rootMC), rootMC);
/* 289 */         break;
/*     */       }
/* 291 */       g = BigDecimal.ONE.subtract(TWO.multiply(x).multiply(v, nMC));
/* 292 */       v = v.add(g.multiply(v, nMC));
/*     */     }
/* 294 */     return x;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 240 */     TWO = new BigDecimal(2);
/*     */   }
/*     */ }