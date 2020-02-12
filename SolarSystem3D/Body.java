package session13;

import java.io.*;
import java.util.*;

public class Body {
    private String name;
    private double mass,orbDistance,vel;
    private Vec3d position; // position of the body in 3D space
    private Vec3d vel3d;    // velocity 3D
    private Vec3d acce;     // acceleration 3D
    
    public Body(){
    }
    public Body(String name, double mass, double orbDistance, double vel, Vec3d x, Vec3d v, Vec3d a){
        this.name = name; 
        this.mass = mass; 
        this.orbDistance = orbDistance; 
        this.vel = vel;
        position = x;  // pick a random spot at the correct orbital distance
        vel3d = v;
        acce = a;
    }
    @Override
    public String toString(){
        return name+"\tmass= "+mass+"\torbDistance= "+orbDistance+"  \tvelocity= "+vel+
        "\ncurrent position 3D: ("+position+")\nvelocity 3D: ("+vel3d+")\nacceleration 3D: ("+acce+")\n";
    }
    
/* Body Bean */
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public double getMass() {return mass;}
    public void setMass(double mass) {this.mass = mass;}

    public double getOrbDistance() {return orbDistance;}
    public void setOrbDistance(double orbDistance) {this.orbDistance = orbDistance;}

    public double getVel() {return vel; }
    public void setVel(double vel) {this.vel = vel;}
    
    public Vec3d getPosition() {return position;}
    public void setPosition(Vec3d position) {this.position = position;}

    public Vec3d getVelDirect() {return vel3d;}
    public void setVelDirect(Vec3d velDirect) {this.vel3d = velDirect;}

    public Vec3d getAcce() {return acce;}
    public void setAcce(Vec3d acce) {this.acce = acce;}
/* Body Bean */
    
    public static Body readFile(int k) throws FileNotFoundException{ // read file and return a body object
        Random r = new Random();
        Scanner s = new Scanner(new FileReader("solarsystem.dat"));
        for(int x = 0; x <= k; x++) 
            s.nextLine();
        String n = s.next(); // name
        s.next();
        double m = Double.parseDouble(s.next()); // mass
        s.next();
        double o = (Double.parseDouble(s.next()) + Double.parseDouble(s.next()))/2; // orbDistance  (r)
        double orbPeriod = Double.parseDouble(s.next());
        double v = (orbPeriod!=0) ? 2*3.1415926*o/(86400*orbPeriod) : 0; // vel
        s.next();
        s.next();
        double orbInclin = Double.parseDouble(s.next());  // theta maximum
        double theta = r.nextDouble()* 2 * orbInclin - orbInclin;  // theta -> [-orbInclin, orbInclin]
        double rCosine = o * Math.cos(theta);
        // 3D position     x^2 + y^2 + z^2 = r^2 (r --> orbDistance)
        double zPosition = o * Math.sin(theta);
        double xPosition = r.nextDouble()* 2 * rCosine - rCosine;
        double yPosition = Math.sqrt(rCosine * rCosine - xPosition * xPosition); // always positive
        if(zPosition < 0)
            yPosition = -yPosition;
        // vel 3D
        double zVel = o*Math.sin(orbInclin)/(43200*orbPeriod);
        double xVel = Math.sqrt(v*v-zVel*zVel)*yPosition/rCosine;
        double yVel = Math.sqrt(v*v-zVel*zVel)*xPosition/rCosine;
        if(xPosition > 0)
            zVel = -zVel;
        // velocity 3D, current position 3D, acceleration 3D
        Vec3d x = new Vec3d(xPosition, yPosition, zPosition);
        Vec3d v3space = new Vec3d(xVel, yVel, zVel);
        Vec3d a = new Vec3d(); // acce is (0,0,0) because we need to get all planets'/satellites' position first
        s.close();
        return new Body(n, m, o, v, x, v3space, a);
    }
    
    public static Vec3d calcu(double mass, Vec3d pos, Vec3d targetPos){ // calculate every object's acceleration
        double G = 6.67408E-11;
        // r^2 : distance between planets
        double r2 = (pos.x-targetPos.x)*(pos.x-targetPos.x)+(pos.y-targetPos.y)*(pos.y-targetPos.y)+(pos.z-targetPos.z)*(pos.z-targetPos.z);
        if(r2 == 0)
            return new Vec3d();
        double aLen = G*mass/r2;
        Vec3d a = Vec3d.subtract(pos, targetPos);
        double mode = Math.sqrt(a.x*a.x+a.y*a.y+a.z+a.z);  // mode : absolute value of acceleration vector
        return new Vec3d(a.x*aLen/mode, a.y*aLen/mode, a.z*aLen/mode);  // return : |a| = Gm/r^2, a*|a|/mode
    }
    
    // static inner class Vec3d
    public static class Vec3d {
        private double x,y,z;
        @Override
        public String toString(){
            return x + " , " + y + " , " + z;
        }

        public Vec3d(double xi , double yi ,double zi){
            x = xi; y = yi; z = zi;
        }
        public Vec3d(){
            this(0,0,0);
        }
        
        public static Vec3d add(Vec3d a, Vec3d b){
            return new Vec3d(a.x+b.x, a.y+b.y, a.z+b.z);
        }
        public static Vec3d subtract(Vec3d a, Vec3d b){
            return new Vec3d(a.x-b.x, a.y-b.y, a.z-b.z);
        }   
        public static double dotProduct(Vec3d a, Vec3d b){
            return a.x*b.x + a.y*b.y +a.z*b.z;
        }   
        public static Vec3d cross(Vec3d a, Vec3d b){
            return new Vec3d(a.y*b.z - a.z*b.y, a.z*b.x - a.x*b.z, a.x*b.y - a.y*b.x); 
        }
        
        void set(int xReset, int yReset, int zReset){
            this.x = xReset;this.y = yReset;this.z = zReset;
        }
    }
}
