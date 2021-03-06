package day05.douyizhu;
/**
* 开始游戏的主类
**/
public class DouDiZhuTest {
    public static void main(String[] args) {
        //创建牌桌
        PaiZhuo paiZhuo = new PaiZhuo();

        //创建三个玩家
        Player player_1 = new Player("大黄", "男", 25, "广西钦州");
        Player player_2 = new Player("大明", "男", 26, "广西梧州");
        Player player_3 = new Player("老毕", "男", 20, "山东济南");

        //创建牌盒
        CardBox cardBox = new CardBox();

        //对局进行中
        paiZhuo.playing(paiZhuo,cardBox, player_1,player_2,player_3);

    }
}
