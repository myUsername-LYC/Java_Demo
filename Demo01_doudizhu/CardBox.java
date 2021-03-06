package day05.douyizhu;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 牌盒类和内部的一个牌类
 */
public class CardBox {
    //定义一个牌的内部类
    public class Card implements Comparable{
        private String number;
        private String color;
        private int color_label;
        private int number_label;

        public Card(String number,String color){
            this.number = number;
            this.color = color;
            switch (color){
                case "♠": this.color_label=4;break;
                case "♥": this.color_label=3;break;
                case "♣": this.color_label=2;break;
                case "♦": this.color_label=1;break;
                default:this.color_label=99;

            }
            switch (number){
                case "3": this.number_label=3;break;
                case "4": this.number_label=4;break;
                case "5": this.number_label=5;break;
                case "6": this.number_label=6;break;
                case "7": this.number_label=7;break;
                case "8": this.number_label=8;break;
                case "9": this.number_label=9;break;
                case "10": this.number_label=10;break;
                case "J": this.number_label=11;break;
                case "Q": this.number_label=12;break;
                case "K": this.number_label=13;break;
                case "A": this.number_label=14;break;
                case "2": this.number_label=15;break;
                case "小王": this.number_label=16;break;
                case "大王": this.number_label=17;break;
            }
        }

        public int getColor_label() {
            return color_label;
        }

        public void setColor_label(int color_label) {
            this.color_label = color_label;
        }

        public int getNumber_label() {
            return number_label;
        }

        public void setNumber_label(int number_label) {
            this.number_label = number_label;
        }

        //看牌
        public String lookCard(){
            return number+color;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Card card = (Card) o;

            if (number != null ? !number.equals(card.number) : card.number != null) return false;
            return color != null ? color.equals(card.color) : card.color == null;
        }

        @Override
        public int hashCode() {
            int result = number != null ? number.hashCode() : 0;
            result = 31 * result + (color != null ? color.hashCode() : 0);
            return result;
        }

        @Override
        public int compareTo(Object o) {
            Card card = (Card)o;
            int num = card.number_label - this.number_label;
            return num==0?card.color_label - this.color_label:num ;
        }

        @Override
        public String toString() {
            return number + color;
        }
    }
    //用于存储牌盒中的牌
    private ArrayList<Card> list;

    //创建对象时做初始化
     {
        list = new MyList<>();
        list.clear();

    }

    public ArrayList<Card> getList() {
        return list;
    }

    public void setList(ArrayList<Card> list) {
        this.list = list;
    }

    //装牌
    public void loadCard(){
        if (list.size()!=0){
            list.clear();
        }
        for (int i = 0; i <4 ; i++) {
            for (int j = 2; j <11 ; j++) {
                Card card = null;
                switch (i){
                    case 0:
                        card = new Card(Integer.toString(j), "♠");
                        break;
                    case 1:
                        card = new Card(Integer.toString(j), "♥");
                        break;
                    case 2:
                        card = new Card(Integer.toString(j), "♣");
                        break;
                    case 3:
                        card = new Card(Integer.toString(j), "♦");
                        break;
                }
                list.add(card);
            }
            switch (i){
                case 0:
                    list.add(new Card("A", "♠"));
                    list.add(new Card("J", "♠"));
                    list.add(new Card("Q", "♠"));
                    list.add(new Card("K", "♠"));
                    break;
                case 1:
                    list.add(new Card("A", "♥"));
                    list.add(new Card("J", "♥"));
                    list.add(new Card("Q", "♥"));
                    list.add(new Card("K", "♥"));
                    break;
                case 2:
                    list.add(new Card("A", "♣"));
                    list.add(new Card("J", "♣"));
                    list.add(new Card("Q", "♣"));
                    list.add(new Card("K", "♣"));
                    break;
                case 3:
                    list.add(new Card("A", "♦"));
                    list.add(new Card("J", "♦"));
                    list.add(new Card("Q", "♦"));
                    list.add(new Card("K", "♦"));
                    break;
            }
        }
        list.add(new Card("大王", ""));
        list.add(new Card("小王", ""));

    }

    //洗牌
    public void shuffle(){
        Collections.shuffle(list);
    }

    //发牌
    public void deal(){
        if (PaiZhuo.getPlayerList().size()!=3){
            System.out.println("人数不足，请等待！");
            return;
        }

        ArrayList<MyList<Card>> playerCard = new MyList<>();
        for (int i = 0; i <3 ; i++) {
            playerCard.add(new MyList<>());
        }

        while (list.size()!=3){
            for (int i = 0; i < playerCard.size(); i++) {
                playerCard.get(i).add(list.remove(0));
            }
        }

        for (int i = 0; i < PaiZhuo.getPlayerList().size(); i++) {
            PaiZhuo.getPlayerList().get(i).quPai(playerCard.get(i));
        }

    }

    //看牌盒里的牌
    public void printCard(){
        for (int i = 0; i < 4; i++) {
            for (int i1 = 0; i1 < 13; i1++) {
                if (i1 == 12){
                    System.out.println(list.get(i*13+i1));
                }else {
                    System.out.print(list.get(i*13+i1)+",");
                }
            }
        }
        System.out.print(list.get(52)+",");
        System.out.print(list.get(53));
     }
}
