package vaninside.mindmirror.Model;

public class StatData {
    //private int resId;
    private int emotion; // 무슨 표정인지.
    private int num; //몇개 있는지
    private int progress = 0;

    private int total;

    public StatData(int emotion, int num, int total)
    {
        this.emotion = emotion;
        this.num = num;
        this.total = total;
    }
    public int getTotal()
    {
        return total;
    }
    public void setProgress(int i){
        progress = i;
    }
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