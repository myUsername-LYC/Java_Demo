package day05.douyizhu;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 玩家类
 */
public class Player {
    //存储玩家姓名
    private String name;
    //存储玩家性别
    private String gender;
    //存储玩家年龄
    private int age;
    //存储玩家所在地区
    private String region;
    //存储玩家手中的牌
    private ArrayList<CardBox.Card> prCard;
    //存储玩家的游戏身份（地主/农民）
    private String identity;

    //创建玩家时的初始化
    {
        prCard = new MyList<>();
        prCard.clear();
        identity = null;
    }

    public Player(){}

    public Player(String name, String gender, int age, String region){
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.region = region;
    }

    public ArrayList<CardBox.Card> getPrCard() {
        return prCard;
    }

    public void setPrCard(ArrayList<CardBox.Card> prCard) {
        this.prCard = prCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getIdentity() { return identity; }

    public void setIdentity(String identity) { this.identity = identity; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (age != player.age) return false;
        if (name != null ? !name.equals(player.name) : player.name != null) return false;
        if (gender != null ? !gender.equals(player.gender) : player.gender != null) return false;
        return region != null ? region.equals(player.region) : player.region == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (region != null ? region.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", region='" + region + '\'' +
                '}';
    }

    //取牌，用于接收发牌
    public void quPai(ArrayList<CardBox.Card> list){
        this.prCard = list;
    }

    //取牌，用于地主的三张牌
    public void quPai(CardBox.Card card){
        this.prCard.add(card);
    }

    //看牌
    public void kanPai(){
        Collections.sort(prCard);
        if (identity == null) {
            System.out.println(this.name+":");
        }else{
            System.out.println(this.name+"("+this.identity+"):");
        }

        System.out.println(prCard);
    }

    //判断能否出牌，true为可出,false为不可出
    public boolean chuPai(ArrayList<CardBox.Card> cards){
        int previousCardIndex = -1; //用于记录最近一次出牌的索引
        ArrayList<CardBox.Card> previous = new ArrayList<>(); //用于记录最近一次的出牌集合
        //找到最近一次出牌的索引
        for (int i = 0; i <2 ; i++) {
            if (PaiZhuo.getIsChuPai()[i] == 1){
                previousCardIndex = i;
                break;
            }
        }
        boolean result = true;
        if (previousCardIndex!=-1){//如果上两家有出牌
            if("不符合出牌规则".equals(PaiZhuo.rule(cards))){
                result = false;
            }else {
                //符合出牌规则
                //不出牌
                if("未出牌".equals(PaiZhuo.rule(cards))){
                    return true;
                }
                //判断自己是否有这个牌；
                for (int i = 0;i<cards.size();i++) {
                    if(!this.prCard.contains(cards.get(i))){
                        return false;
                    }
                }
                //判断要出的牌是否比最近一次出的牌大
                previous = PaiZhuo.getRecentCard().get(previousCardIndex);
                if(!PaiZhuo.compareCard(previous, cards)){
                    //比上次出牌小就不可出
                    result = false;
                }
            }
        }else{
            //上两家都没有出牌，我们必须要出牌
            if("不符合出牌规则".equals(PaiZhuo.rule(cards))){
                result = false;
            }
            //判断自己是否有这个牌；
            for (int i = 0;i<cards.size();i++) {
                if(!this.prCard.contains(cards.get(i))){
                    return false;
                }
            }
            if ("未出牌".equals(PaiZhuo.rule(cards))){
               result = false;
            }
        }
        return result;
    }

    //出牌,将要出的牌从自己持牌的集合中移除，成功返回true，失败返回false
    public boolean post(ArrayList<CardBox.Card> cards){
        for (int i = 0; i < cards.size(); i++) {
            if (!this.prCard.remove(cards.get(i))){
                return false;
            }
        }
        return true;
    }

    //抢地主
    public  boolean qiangDiZhu(String s, int index){
        boolean result ;
        if ("抢地主".equals(s)){
            PaiZhuo.getIsQiangDiZhu()[index] = PaiZhuo.getIsQiangDiZhu()[index]+1;
            result = true;
        }else if("不抢".equals(s)){
            result = true;
        }else{
            System.out.println("输入错误!,请以指定格式重新输入:抢地主/不抢");
            result = false;
        }
        return result;
    }
}
