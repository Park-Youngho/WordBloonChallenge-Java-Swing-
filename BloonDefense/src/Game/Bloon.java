package Game;
import java.awt.*;

public class Bloon {
    private String word; //풍선에 달 단어
    private int x, y; //위치
    private int speed; //풍선 속도
    private Image image; //풍선이미지

    public Bloon(String word, Image image, int x, int y, int speed){
        this.word = word;
        this.image = image;
        this.x = x;
        this.y =y;
        this.speed = speed;


    }

    public void move(){
        y -= speed;
    }
    public String getWord(){
        return word;
    }
    public boolean outOfScreen(){ 
        return y + image.getHeight(null) < 0; //화면 밖으로 나가면 true반환
    }
    public Image getBloonImage(){
        return image;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
