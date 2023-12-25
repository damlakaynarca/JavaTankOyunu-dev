package application;
//Main.java
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;

public class Main extends Application {

 // Tank.java
 public class Tank {
     private Circle body; // tankın gövdesi
     private Line barrel; // tankın namlusu
     private double x; // tankın x konumu
     private double y; // tankın y konumu
     private double angle; // tankın yön açısı
     private double speed; // tankın hızı
     private double barrelAngle; // tankın namlu açısı

     public Tank(Circle body, Line barrel, double x, double y, double angle, double speed, double barrelAngle) {
         this.body = body;
         this.barrel = barrel;
         this.x = x;
         this.y = y;
         this.angle = angle;
         this.speed = speed;
         this.barrelAngle = barrelAngle;
         update();
     }

     public Circle getBody() {
         return body;
     }

     public Line getBarrel() {
         return barrel;
     }

     public double getX() {
         return x;
     }

     public double getY() {
         return y;
     }

     public double getAngle() {
         return angle;
     }

     public double getSpeed() {
         return speed;
     }

     public double getBarrelAngle() {
         return barrelAngle;
     }

     public void setX(double x) {
         this.x = x;
         update();
     }

     public void setY(double y) {
         this.y = y;
         update();
     }

     public void setAngle(double angle) {
         this.angle = angle;
         update();
     }

     public void setSpeed(double speed) {
         this.speed = speed;
     }

     public void setBarrelAngle(double barrelAngle) {
         this.barrelAngle = barrelAngle;
         update();
     }

     // tankın konumunu, yönünü ve namlu açısını güncelle
     public void update() {
         body.setCenterX(x);
         body.setCenterY(y);
         body.setRotate(angle);
         barrel.setStartX(x);
         barrel.setStartY(y);
         barrel.setEndX(x + Math.cos(Math.toRadians(barrelAngle)) * 20);
         barrel.setEndY(y + Math.sin(Math.toRadians(barrelAngle)) * 20);
         barrel.setRotate(angle);
     }

     // tankı ileri hareket ettirir
     public void moveForward() {
         x += Math.cos(Math.toRadians(angle)) * speed;
         y += Math.sin(Math.toRadians(angle)) * speed;
         update();
     }

     // tankı geri hareket ettirir
     public void moveBackward() {
         x -= Math.cos(Math.toRadians(angle)) * speed;
         y -= Math.sin(Math.toRadians(angle)) * speed;
         update();
     }

     // tankı sola döndürür
     public void turnLeft() {
         angle = (angle - 5) % 360;
         update();
     }

     // tankı sağa döndürür
     public void turnRight() {
         angle = (angle + 5) % 360;
         update();
     }

     // tankın namlusunu yukarı kaldırır
     public void raiseBarrel() {
         barrelAngle = (barrelAngle + 5) % 360;
         update();
     }

     // tankın namlusunu aşağı indirir
     public void lowerBarrel() {
         barrelAngle = (barrelAngle - 5) % 360;
         update();
     }
 }

 // Bullet.java
 public class Bullet {
     private Circle body; // mermi gövdesi
     private double x; // mermi x konumu
     private double y; // mermi y konumu
     private double angle; // mermi yön açısı
     private double speed; // mermi hızı

     public Bullet(Circle body, double x, double y, double angle, double speed) {
         this.body = body;
         this.x = x;
         this.y = y;
         this.angle = angle;
         this.speed = speed;
         update();
     }

     public Circle getBody() {
         return body;
     }

     public double getX() {
         return x;
     }

     public double getY() {
         return y;
     }

     public double getAngle() {
         return angle;
     }

     public double getSpeed() {
         return speed;
     }

     // mermiyi ileri hareket ettirir
     public void move() {
         x += Math.cos(Math.toRadians(angle)) * speed;
         y += Math.sin(Math.toRadians(angle)) * speed;
         update();
     }

     // mermiyi konumunu günceller
     public void update() {
         body.setCenterX(x);
         body.setCenterY(y);
     }
 }

 // oyunun genişliği ve yüksekliği
 private final int WIDTH = 800;
 private final int HEIGHT = 600;

 // oyunun sahnesi
 private Scene scene;

 // oyunun paneli
 private Pane pane;

 // oyuncu tankı
 private Tank player;

 // düşman tankı
 private Tank enemy;

 // oyuncu mermileri
 private ArrayList<Bullet> playerBullets;

 // düşman mermileri
 private ArrayList<Bullet> enemyBullets;

 // oyun döngüsü
 private AnimationTimer timer;

 @Override
 public void start(Stage primaryStage) throws Exception {
     // oyun panelini oluştur
     pane = new Pane();
     pane.setPrefSize(WIDTH, HEIGHT);

     // oyuncu tankını oluştur
     player = new Tank(new Circle(20, Color.BLUE), new Line(0, 0, 20, 0), 100, 100, 0, 5, 0);
     pane.getChildren().addAll(player.getBody(), player.getBarrel());

     // düşman tankını oluştur
     enemy = new Tank(new Circle(20, Color.RED), new Line(0, 0, 20, 0), 700, 500, 180, 5, 180);
     pane.getChildren().addAll(enemy.getBody(), enemy.getBarrel());

     // oyuncu ve düşman mermilerini oluştur
     playerBullets = new ArrayList<>();
     enemyBullets = new ArrayList<>();

     // oyun sahnesini oluştur
     scene = new Scene(pane);

     // oyun sahnesine klavye girdisi ekle
     scene.setOnKeyPressed(event -> {
         // oyuncu tankını kontrol et
         if (event.getCode() == KeyCode.W) {
             player.moveForward();
         } else if (event.getCode() == KeyCode.S) {
             player.moveBackward();
         } else if (event.getCode() == KeyCode.A) {
             player.turnLeft();
         } else if (event.getCode() == KeyCode.D) {
             player.turnRight();
         } else if (event.getCode() == KeyCode.Q) {
             player.raiseBarrel();
         } else if (event.getCode() == KeyCode.E) {
             player.lowerBarrel();
         } else if (event.getCode() == KeyCode.SPACE) {
             // oyuncu tankından mermi at
             Bullet bullet = new Bullet(new Circle(5, Color.BLUE), player.getX(), player.getY(), player.getAngle() + player.getBarrelAngle(), 10);
             playerBullets.add(bullet);
             pane.getChildren().add(bullet.getBody());
         }
     });

     // oyun döngüsünü oluştur
     timer = new AnimationTimer() {
         @Override
         public void handle(long now) {
             // oyun mantığını güncelle
             updateGameLogic();
         }
     };

     // oyun döngüsünü başlat
     timer.start();

     // oyun sahnesini göster
     primaryStage.setScene(scene);
     primaryStage.setTitle("Tank Oyunu");
     primaryStage.show();
 }

 // oyun mantığını güncelleyen metod
 public void updateGameLogic() {
     // oyuncu ve düşman mermilerini hareket ettir
     for (Bullet bullet : playerBullets) {
         bullet.move();
     }
     for (Bullet bullet : enemyBullets) {
         bullet.move();
     }}

  // oyuncu ve düşman mermilerinin sınırları aşmasını veya çarpışmasını kontrol eden metod
     public void checkBullets() {
         // oyuncu mermilerini kontrol et
         Iterator<Bullet> playerBulletIterator = playerBullets.iterator();
         while (playerBulletIterator.hasNext()) {
             Bullet bullet = playerBulletIterator.next();
             // eğer mermi sınırları aştıysa, listeden ve panelden kaldır
             if (bullet.getX() < 0 || bullet.getX() > WIDTH || bullet.getY() < 0 || bullet.getY() > HEIGHT) {
                 playerBulletIterator.remove();
                 pane.getChildren().remove(bullet.getBody());
             }
             // eğer mermi düşman tankına çarptıysa, listeden ve panelden kaldır ve oyunu bitir
             if (bullet.getBody().intersects(enemy.getBody().getBoundsInLocal())) {
                 playerBulletIterator.remove();
                 pane.getChildren().remove(bullet.getBody());
                 timer.stop();
                 System.out.println("Oyuncu kazandı!");
             }
         }
         // düşman mermilerini kontrol et
         Iterator<Bullet> enemyBulletIterator = enemyBullets.iterator();
         while (enemyBulletIterator.hasNext()) {
             Bullet bullet = enemyBulletIterator.next();
             // eğer mermi sınırları aştıysa, listeden ve panelden kaldır
             if (bullet.getX() < 0 || bullet.getX() > WIDTH || bullet.getY() < 0 || bullet.getY() > HEIGHT) {
                 enemyBulletIterator.remove();
                 pane.getChildren().remove(bullet.getBody());
             }
             // eğer mermi oyuncu tankına çarptıysa, listeden ve panelden kaldır ve oyunu bitir
             if (bullet.getBody().intersects(player.getBody().getBoundsInLocal())) {
                 enemyBulletIterator.remove();
                 pane.getChildren().remove(bullet.getBody());
                 timer.stop();
                 System.out.println("Düşman kazandı!");
             }
         }
     }}
