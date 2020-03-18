package vaninside.mindmirror.Model;

public class StatData {
    private int resId;
    private int emotion; // 무슨 표정인지.
    private int num; //몇개 있는지

    public int getEmotion(){
        return emotion;
    }

    public void setEmotion(int i){
        this.emotion = i;
    }

    public int getNum(){
        return num;
    }

    public void setNum(int i){
        this.num = i;
    }

}
