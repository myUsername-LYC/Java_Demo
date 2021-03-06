package day05.douyizhu;

import java.util.*;

/**
 * 牌桌类
 */
public class PaiZhuo {
    //存储已经打出的牌
    private static HashSet<CardBox.Card> set ;
    //上两次是否出牌(索引0是上次，索引1是上上次，值0是不出牌，，值1是出牌)
    private static int[] isChuPai;
    //最近两次出牌(索引0是上次，索引1是上上次)
    private static ArrayList<ArrayList<CardBox.Card>> recentCard;
    //用于记录三个人是否抢地主（抢则值+1，不抢不变）
    private static int[] isQiangDiZhu;
    //用于储存牌桌的玩家
    private static ArrayList<Player> playerList;

    //创建牌桌时的静态初始化
    static {
        set = new HashSet<>();
        set.clear();
        isChuPai = new int[2];
        for (int i = 0; i < isChuPai.length; i++) {
            isChuPai[i]=0;
        }
        recentCard = new ArrayList<>();
        recentCard.add(new ArrayList<>());
        recentCard.add(new ArrayList<>());
        isQiangDiZhu = new int[3];
        for (int i = 0; i < isQiangDiZhu.length; i++) {
            isQiangDiZhu[i]=0;
        }
        playerList = new ArrayList<>();
        playerList.clear();
    }

    public static ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public static void setPlayerList(ArrayList<Player> playerList) {
        PaiZhuo.playerList = playerList;
    }

    public PaiZhuo() {
    }

    public static HashSet<CardBox.Card> getSet() {
        return set;
    }

    public static void setSet(HashSet<CardBox.Card> set) {
        PaiZhuo.set = set;
    }

    public static int[] getIsChuPai() {
        return isChuPai;
    }

    public static void setIsChuPai(int[] isChuPai) {
        PaiZhuo.isChuPai = isChuPai;
    }

    public static ArrayList<ArrayList<CardBox.Card>> getRecentCard() {
        return recentCard;
    }

    public static void setRecentCard(ArrayList<ArrayList<CardBox.Card>> recentCard) {
        PaiZhuo.recentCard = recentCard;
    }

    public static int[] getIsQiangDiZhu() {
        return isQiangDiZhu;
    }

    public static void setIsQiangDiZhu(int[] isQiangDiZhu) {
        PaiZhuo.isQiangDiZhu = isQiangDiZhu;
    }

    //添加玩家
    public void addPlayer(Player player){
        PaiZhuo.playerList.add(player);
    }

    /**确认谁是地主
     * 返回一个长度为3的int数组，第一位数代表是否确定了地主（1为确定，0为未确定）
     * 第二位数为地主的索引（确定地主时）或为抢地主的索引（未确定地主时）
     * 第三位数代表是否重新开始
     * 参数flag表示抢地主的轮数
     **/
    public  int[] dizhu(int flag){
        int[] result = new int[3];
        for (int i=0 ;i < result.length;i++){
            result[i] = -1;
        }
        result[2] = 0;
        if(flag == 1){ //是否第一次抢地主
            int count = 0; //每轮抢地主的人数
            int sign_1 = -1; //地主玩家在playerList中的索引
            int sign_0 = -1; //上轮没有抢地主的玩家在playerList中的索引
            for (int i = 0; i < 3; i++){
                if (isQiangDiZhu[i]==1){
                    count++;
                    sign_1 = i;
                }else if (isQiangDiZhu[i]==0){
                    sign_0 = i;
                }
            }
            if(count == 0){
                //无人抢地主，重新开始
                result[2] = 1;
                return result;
            }else if (count == 1) {
                //确认地主
                result[0] = 1;
                result[1] = sign_1 ;
                return result;
            }else{
                //开始下一轮抢地主
                if(count == 2){ //两人抢了地主
                    result[0] = 0;
                    result[1] = sign_0 ;
                }else{   //三人抢了地主
                    result[0] = 0;
                }
            }
        }else{  //第二次抢地主
            for (int i = 0; i < 3; i++) {
                if(isQiangDiZhu[i]==2){
                    //确认地主
                    result[0] = 1;
                    result[1] = i;
                    return result;
                }
            }
        }
        return result;
    }

    //将打出的牌存起来
    public static void cunPai(ArrayList<CardBox.Card> recentList){
        for (CardBox.Card card : recentList) {
            PaiZhuo.set.add(card);
        }
    }

    //更新最近两次的出牌情况
    public static void updateRecent(ArrayList<CardBox.Card> recentList){
        isChuPai[1] = isChuPai[0];
        recentCard.set(1,recentCard.get(0));
        if (recentList.size() == 0) {
            isChuPai[0] = 0;
        }else {
            isChuPai[0] = 1;
        }
        recentCard.set(0,recentList);
    }

    //开始游戏,结果返回true表示成功开始，返回false表示重开。
    public boolean startGame(PaiZhuo paiZhuo,CardBox cardBox, Player player_1,Player player_2,Player player_3){
        //每开始一局新的游戏都要进行初始化
        {
            //牌桌初始化
            PaiZhuo.getPlayerList().clear();
            PaiZhuo.set.clear();
            ArrayList<CardBox.Card> cards1 = new ArrayList<>();
            ArrayList<CardBox.Card> cards2 = new ArrayList<>();
            cards1.clear();
            cards2.clear();
            PaiZhuo.recentCard.add(cards1);
            PaiZhuo.recentCard.add(cards2);
            for (int i = 0; i < isChuPai.length; i++) {
                isChuPai[i] = 0;
            }
            for (int i = 0; i < PaiZhuo.isQiangDiZhu.length; i++) {
                PaiZhuo.isQiangDiZhu[i] = 0 ;
            }
            //玩家初始化
            player_1.getPrCard().clear();
            player_2.getPrCard().clear();
            player_3.getPrCard().clear();
            player_1.setIdentity(null);
            player_2.setIdentity(null);
            player_3.setIdentity(null);

            //牌盒
            cardBox.getList().clear();
        }

        //三个玩家添加到牌桌上
        paiZhuo.addPlayer(player_1);
        paiZhuo.addPlayer(player_2);
        paiZhuo.addPlayer(player_3);

        //装牌和洗牌
        cardBox.loadCard();
//调试        cardBox.printCard();
        cardBox.shuffle();
//调试        cardBox.printCard();
        ArrayList<CardBox.Card> list = cardBox.getList();

        //发牌和看牌
//调试        System.out.println(list.size());
        cardBox.deal();
        player_1.kanPai();
        player_2.kanPai();
        player_3.kanPai();
        System.out.println();
//调试        System.out.println(list.size());


        /**
         * 用一个长度为3的数组a记录每个人是否抢地主，用一个长度为3的数组b记录抢地主的结果。
         * 抢地主分为两轮
         * 第一轮：从任意一个人开始，每个人一次机会（一共三个人）
         *     分为四种情况：都不抢、一个人抢，两个人抢，三个人抢
         * 第二轮：只有一个人能抢，也就是第一轮中第一个抢地主的人
         * */
        //开始抢地主
        System.out.println("开始抢地主！");
        int qiangCount = 1; //表示第几轮抢地主
        int[] isDiZhu = new int[3]; //用于接收表示三人抢地主结果的数组
        for (int i = 0; i < isDiZhu.length; i++) {
            isDiZhu[i] = 0;
        }
        int random = (int) (Math.random() * 3);
        Scanner sc = new Scanner(System.in);
        while (true) {
            if (qiangCount == 1) {  //第一轮抢地主
                for (int i = random; i < random + 3; i++) {
                    while (true) {
                        System.out.println(PaiZhuo.getPlayerList().get(i % 3).getName() + ",请输入是否抢地主:抢地主/不抢");
                        if (PaiZhuo.getPlayerList().get(i % 3).qiangDiZhu(sc.nextLine(), i % 3))
                            break;
                    }
                }
                isDiZhu = paiZhuo.dizhu(qiangCount); //进行第一轮的判断
            } else { //第二轮抢地主
                String s = "";
                for (int i = random; i < random + 2; i++) {
                    if ((i % 3) != isDiZhu[1]) { //第二轮只有第一个抢地主的人。
                        while (true) {
                            System.out.println(PaiZhuo.getPlayerList().get(i % 3).getName() + ",请输入是否抢地主:抢地主/不抢");
                            s = sc.nextLine();
                            if (PaiZhuo.getPlayerList().get(i % 3).qiangDiZhu(s, i % 3))
                                break;
                        }
                    } else {
                        continue;
                    }
                    //第二轮只有一个人
                    if ("抢地主".equals(s)) {
                        break;
                    } else if ("不抢".equals(s)) {
                        for (int j = i - 1; j > i - 3; j--) {
                            //为了避免出现负数
                            if (PaiZhuo.getIsQiangDiZhu()[(j + 3) % 3] == 1) {
                                PaiZhuo.getIsQiangDiZhu()[(j + 3) % 3]++;
                                break;
                            }
                        }
                        break;
                    }
                }
                isDiZhu = paiZhuo.dizhu(qiangCount); //进行第二轮的结果判断
            }
            if (isDiZhu[2] == 1) {
                //重开
                return false;
            } else if (isDiZhu[0] == 1) {
                //确定地主
                for (int i = 0; i < 3; i++) {
                    if (isDiZhu[1] == i) {
                        PaiZhuo.getPlayerList().get(i).setIdentity("地主");
                    } else {
                        PaiZhuo.getPlayerList().get(i).setIdentity("农民");
                    }
                }
                break;
            }
            qiangCount++;
        }

        //地主获取地主牌
        for (int i = 0; i < 3; i++) {
            if ("地主".equals(PaiZhuo.getPlayerList().get(i).getIdentity())) {
                while (cardBox.getList().size() != 0) {
                    PaiZhuo.getPlayerList().get(i).quPai(cardBox.getList().remove(0));
                }
            }
        }
        player_1.kanPai();
        player_2.kanPai();
        player_3.kanPai();
        return true;
    }

    //对局进行中
    public void playing(PaiZhuo paiZhuo ,CardBox cardBox, Player player_1,Player player_2,Player player_3){
        Scanner sc = new Scanner(System.in);
        //循环进行游戏，如果不玩了跳出循环
        ENDGAME:
        while (true) {


            //游戏开始,没有确定地主就重开
            while (!paiZhuo.startGame(paiZhuo, cardBox, player_1, player_2, player_3));

            //地主开始出牌
            int count = 0;
            for (int i = 0; i < 3; i++) {
                if ("地主".equals(PaiZhuo.getPlayerList().get(i).getIdentity())) {
                    count = i;
                    break;
                }
            }
            System.out.println();
            System.out.println("请以指定格式输入你的出牌(例：\"3♥,3♣,3♦,3♠\",其中不包括双引号)");
            System.out.println("要不起请直接按回车键跳过");
            //每个玩家依次、轮流出牌，直到某个玩家的牌出完了结束当前对局。
            while (true) {
                Player player = PaiZhuo.getPlayerList().get(count);//正在出牌的玩家
                System.out.println(player.getName() +
                        ",请输入你的出牌(要不起请直接按回车键跳过):");
                ArrayList<CardBox.Card> cards;
                //输入出牌或按回车键跳过
                //获取到合理的出牌操作并且处理完之后才能停止
                do {
                    //获取到能正确解析的输入才能停止
                    while (true) {
                        String s = sc.nextLine();
                        cards = parseString(s);
                        if (cards != null) break;
                    }
                    String result = PaiZhuo.rule(cards);

//                    System.out.println(result);//输出测试

                    if (player.chuPai(cards)) {
                        // 输入可行，处理后跳出循环
                        if("未出牌".equals(result)){
                            //没有出牌
                            System.out.println(player.getName()+"要不起！");
                            PaiZhuo.updateRecent(cards);
                            //输出测试
//                             for (int i = 0; i < PaiZhuo.getIsChuPai().length; i++) {
//                                 System.out.println("IsChuPai()["+i+"]"+PaiZhuo.getIsChuPai()[i]);
//                             }
                            System.out.println("上一次出牌："+PaiZhuo.getRecentCard().get(0));
                            System.out.println("上上次出牌："+PaiZhuo.getRecentCard().get(1));

                        }else {
                            //出了牌
                            System.out.println(player.getName()+"出牌:"+cards);
                            PaiZhuo.updateRecent(cards);
                            PaiZhuo.cunPai(cards);
                            //输出测试
//                             System.out.println("已经出牌："+PaiZhuo.getSet());
//                             for (int i = 0; i < PaiZhuo.getIsChuPai().length; i++) {
//                                 System.out.println("IsChuPai()["+i+"]"+PaiZhuo.getIsChuPai()[i]);
//                             }
                            System.out.println("上一次出牌："+PaiZhuo.getRecentCard().get(0));
                            System.out.println("上上次出牌："+PaiZhuo.getRecentCard().get(1));
                            boolean post = player.post(cards);
//                             System.out.println(post);

                        }
                        break;
                    }else{
                        //
                        if ("未出牌".equals(result)){
                            System.out.println(player.getName()+",该你出牌啦!");
                        }else {
                            System.out.println(player.getName()+",你出错牌啦!");
                        }

                    }
                }while (true);
                //判断刚出牌的人是否打完了，是则当前对局结束。
                if (player.getPrCard().size() == 0){
                    System.out.println(player.getName()+"打完啦!");
                    System.out.println(player.getIdentity()+"获得了对局胜利!");
                    break;
                }
                player_1.kanPai();
                player_2.kanPai();
                player_3.kanPai();
                System.out.println();
                //下一个玩家出牌
                count++;
                count%=3;
            }
            //上个对局结束了，是否开启新对局
            System.out.println("是否开始新的一局?再来一局or不玩了");
            //获取到正确的输入才会跳出并执行操作
            do {
                String s = sc.nextLine();
                if ("再来一局".equals(s)) {
                    //开始新的对局
                    System.out.println("即将开始!");
                    break;
                } else if ("不玩了".equals(s)) {
                    //游戏结束，退出进程
                    System.out.println("返回游戏大厅!");
                    break ENDGAME;
                } else {
                    System.out.println("输入无效,请重新输入:再来一局or不玩了");
                }
            }while(true);
        }
    }

    //出牌规则，返回一个出牌类型的字符串
    public static String rule(ArrayList<CardBox.Card> cards){
        int cardNumber = cards.size();
        String cardType = "";
        CardBox.Card firstcard ;
        CardBox.Card lastcard ;
        int firstIndex = 0;
        int lastIndex = 0;
        int count = 0;
        //创建一个忽略花色，比较数值是否相等的比较器
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                CardBox.Card card1 = (CardBox.Card) o1;
                CardBox.Card card2 = (CardBox.Card) o2;
                return card1.getNumber_label()-card2.getNumber_label();
            }
        };
        //先对牌进行排序
        Collections.sort(cards);
        switch (cardNumber){
            case 0://未出牌
                cardType = "未出牌";
                break;
            case 1://一张：单张
                cardType = "单张";
                break;
            case 2://两张：一对，王炸
                if(comparator.compare(cards.get(0),cards.get(1)) == 0){
                    cardType = "一对";
                }else if(cards.contains(new CardBox().new Card("大王","")) &&
                         cards.contains(new CardBox().new Card("小王",""))){
                    cardType = "王炸";
                }else{
                    //不符合出牌规则
                        cardType = "不符合出牌规则";
                }
                break;
            case 3://三张：三不带
                firstcard = cards.get(0);
                for (firstIndex = 1; firstIndex < 3; firstIndex++) {
                    if (comparator.compare(firstcard,cards.get(firstIndex))!=0){
                        break;
                    }
                }
                if(firstIndex == 3){
                    cardType = "三不带";
                }else {
                    //不符合出牌规则
                    cardType = "不符合出牌规则";
                }
                break;
            case 4://四张：三带一，炸弹
                firstcard = cards.get(0);
                for (firstIndex = 1; firstIndex < 4; firstIndex++) {
                    if (comparator.compare(firstcard,cards.get(firstIndex))!=0){
                        break;
                    }
                }
                lastcard = cards.get(3);
                for (lastIndex = 2; lastIndex > -1; lastIndex--) {
                    if (comparator.compare(lastcard,cards.get(lastIndex))!=0){
                        break;
                    }
                }
                //全都相等则为炸弹，如果三个相等则为三带一，否则不符合出牌规则
                if (firstIndex == 4){
                    cardType = "炸弹";
                }else if((firstIndex == 3 && lastIndex == 2) || (firstIndex == 1 && lastIndex == 0)){
                    cardType = "三带一";
                }else{
                    //出牌不符合规则
                    cardType = "不符合出牌规则";
                }
                break;
            case 5://五张：三带一对，顺子；
                firstcard = cards.get(0);
                for ( firstIndex = 1; firstIndex < 5; firstIndex++) {
                    if (comparator.compare(firstcard,cards.get(firstIndex))!=0){
                        break;
                    }
                }
                lastcard = cards.get(4);
                for (lastIndex = 3; lastIndex > -1; lastIndex--) {
                    if (comparator.compare(lastcard,cards.get(lastIndex))!=0){
                        break;
                    }
                }
                //3+2或者2+3则为三带一对，
                if ((firstIndex == 3 && lastIndex == 2) || (firstIndex == 2 && lastIndex == 1)){
                    cardType = "三带一对";
                }else{
                    //判断它是否顺子
                    for (int i = 0; i<4; i++){
                        count = i;
                        if (cards.get(i).getNumber_label()>14){
                            break;
                        }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 1){
                            break;
                        }
                        count = i+1;
                    }
                    if (count == 4){
                        cardType = "顺子";
                    }else {
                        //不符合出牌规则
                        cardType = "不符合出牌规则";
                    }
                }
                break;
            case 6://六张：四带二，顺子，连对(三对)
                for (lastIndex = 0; lastIndex <3 ; lastIndex++) {
                    firstcard = cards.get(lastIndex);
                    for ( firstIndex = lastIndex+1; firstIndex < lastIndex+4; firstIndex++) {
                        if (comparator.compare(firstcard,cards.get(firstIndex))!=0){
                            break;
                        }
                    }
                    if(firstIndex == lastIndex+4){
                        break;
                    }
                }
                //存在4个相同则为四带二，
                if (lastIndex < 3){
                    cardType = "四带二";
                }else {
                    //判断是否是连对
                    for (int i = 0; i<5; i+=2){
                        count = i;
                        if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                            break;
                        }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0){
                            //如果不是每两个都相等的不行
                            break;
                        }else if(i <4 && cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 1 ){
                            //短路与，判断对是否连续
                            break;
                        }
                        count = i+2;
                    }
                    if (count == 6){
                        cardType = "连对(三对)";
                    }else {
                        //判断它是否顺子
                        for (int i = 0; i<5; i++){
                            count = i;
                            if (cards.get(i).getNumber_label()>14){
                                break;
                            }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 1){
                                break;
                            }
                            count = i+1;
                        }
                        if (count == 5){
                            cardType = "顺子";
                        }else {
                            //不符合出牌规则
                            cardType = "不符合出牌规则";
                        }
                    }
                }
                break;
            case 7://七张：顺子
                for (int i = 0; i<6; i++){
                    count = i;
                    if (cards.get(i).getNumber_label()>14){
                        break;
                    }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 1){
                        break;
                    }
                    count = i+1;
                }
                if (count == 6){
                    cardType = "顺子";
                }else {
                    //不符合出牌规则
                    cardType = "不符合出牌规则";
                }
                break;
            case 8://八张：四带两对，顺子，飞机(两个三带一)，连对(四个)
                    /*
                    firstcard = previous.get(0);
                    for ( firstIndex = 1; firstIndex < 4; firstIndex++) {
                        if (comparator.compare(firstcard,previous.get(firstIndex))!=0){
                            break;
                        }
                    }
                    if(firstIndex == 4){
                    }
                    firstcard = previous.get(2);
                    for ( firstIndex = 3; firstIndex < 6; firstIndex++) {
                        if (comparator.compare(firstcard,previous.get(firstIndex))!=0){
                            break;
                        }
                    }
                    if(firstIndex == 6){
                    }
                    firstcard = previous.get(4);
                    for ( firstIndex = 5; firstIndex < 8; firstIndex++) {
                        if (comparator.compare(firstcard,previous.get(firstIndex))!=0){
                            break;
                        }
                    }
                    if(firstIndex == 8){
                    }
                    */
                for (lastIndex = 0; lastIndex <5 ; lastIndex+=2) {
                    firstcard = cards.get(lastIndex);
                    for ( firstIndex = lastIndex+1; firstIndex < lastIndex+4; firstIndex++) {
                        if (comparator.compare(firstcard,cards.get(firstIndex))!=0){
                            break;
                        }
                    }
                    if(firstIndex == lastIndex+4){
                        break;
                    }
                }
                if(lastIndex == 0){
                    if(comparator.compare(cards.get(4),cards.get(5)) == 0 &&
                       comparator.compare(cards.get(6),cards.get(7)) == 0){
                        cardType = "四带两对";
                    }else{
                        //不符合出牌规则
                        cardType = "不符合出牌规则";
                    }
                }else if(lastIndex == 2){
                    if(comparator.compare(cards.get(0),cards.get(1)) == 0 &&
                       comparator.compare(cards.get(6),cards.get(7)) == 0){
                        cardType = "四带两对";
                    }else{
                        //不符合出牌规则
                        cardType = "不符合出牌规则";
                    }
                }else if(lastIndex == 4){
                    if(comparator.compare(cards.get(0),cards.get(1)) == 0 &&
                       comparator.compare(cards.get(2),cards.get(3)) == 0){
                        cardType = "四带两对";
                    }else{
                        //不符合出牌规则
                        cardType = "不符合出牌规则";
                    }
                }else if(lastIndex == 6){
                    //不是四带两对
                    for (int i = 0;  i<3 ; i++) {
                        //判断是否飞机
                        count = i;
                        firstcard = cards.get(i);
                        for (firstIndex = i+1;firstIndex<i+3;firstIndex++){
                            if (comparator.compare(firstcard,cards.get(firstIndex))!=0){
                                break;
                            }
                        }
                        lastcard = cards.get(i+3);
                        for (lastIndex = i+4;lastIndex<i+6;lastIndex++){
                            if (comparator.compare(lastcard,cards.get(lastIndex))!=0){
                                break;
                            }
                        }
                        if (firstIndex == i+3 && lastIndex == i+6 && comparator.compare(firstcard,lastcard) == 1){
                            break;
                        }
                        count = i+1;
                    }
                    if (count < 3){
                        cardType = "飞机(两个三带一)";
                    }else{
                        //判断是否是连对
                        for (int i = 0; i<7; i+=2){
                            count = i;
                            if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                                break;
                            }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0){
                                //如果不是每两个都相等的不行
                                break;
                            }else if(i <6 && cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 1 ){
                                //短路与，判断对是否连续
                                break;
                            }
                            count = i+2;
                        }
                        if (count == 8){
                            cardType = "连对(四对)";
                        }else {
                            //判断是否顺子
                            for (int i = 0; i<7; i++){
                                count = i;
                                if (cards.get(i).getNumber_label()>14){
                                    break;
                                }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 1){
                                    break;
                                }
                                count = i+1;
                            }
                            if (count == 7){
                                cardType = "顺子";
                            }else {
                                //不符合出牌规则
                                cardType = "不符合出牌规则";
                            }
                        }
                    }
                }
                break;
            case 9://九张：顺子，三个三不带
                for (int i = 0; i<7; i+=3){
                    count = i;
                    if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                        break;
                    }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0 ||
                            cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 0 ){
                        //如果不是每三个都相等的不行
                        break;
                    }else if(i <6 && cards.get(i).getNumber_label()-cards.get(i+3).getNumber_label() != 1 ){
                        //短路与，判断对是否连续
                        break;
                    }
                    count = i+3;
                }
                if (count == 9){
                    cardType = "三个三不带";
                }else{
                    for (int i = 0; i<8; i++){
                        count = i;
                        if (cards.get(i).getNumber_label()>14){
                            break;
                        }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 1){
                            break;
                        }
                        count = i+1;
                    }
                    if (count == 8){
                        cardType = "顺子";
                    }else {
                        //不符合出牌规则
                        cardType = "不符合出牌规则";
                    }
                }
                break;
            case 10://十张：顺子，连对(五个)，飞机(两个三带一对)
                for (int i = 0;  i<5 ; i+=2) {
                    //判断是否飞机
                    count = i;
                    firstcard = cards.get(i);
                    for (firstIndex = i+1;firstIndex<i+3;firstIndex++){
                        if (comparator.compare(firstcard,cards.get(firstIndex))!=0){
                            break;
                        }
                    }
                    lastcard = cards.get(i+3);
                    for (lastIndex = i+4;lastIndex<i+6;lastIndex++){
                        if (comparator.compare(lastcard,cards.get(lastIndex))!=0){
                            break;
                        }
                    }
                    if (firstIndex == i+3 && lastIndex == i+6 && comparator.compare(firstcard,lastcard) == 1){
                        break;
                    }
                    count = i+2;
                }
                if (count == 0){
                    if(comparator.compare(cards.get(6),cards.get(7)) == 0 &&
                       comparator.compare(cards.get(8),cards.get(9)) == 0){
                        cardType = "飞机(两个三带一对)";
                    }else{
                        //不符合出牌规则
                        cardType = "不符合出牌规则";
                    }
                }else if(count == 2){
                    if(comparator.compare(cards.get(0),cards.get(1)) == 0 &&
                       comparator.compare(cards.get(8),cards.get(9)) == 0){
                        cardType = "飞机(两个三带一对)";
                    }else{
                        //不符合出牌规则
                        cardType = "不符合出牌规则";
                    }
                }else if(count == 4){
                    if(comparator.compare(cards.get(0),cards.get(1)) == 0 &&
                       comparator.compare(cards.get(2),cards.get(3)) == 0){
                        cardType = "飞机(两个三带一对)";
                    }else{
                        //不符合出牌规则
                        cardType = "不符合出牌规则";
                    }
                }else{
                    //不是飞机，判断是否连对
                    for (int i = 0; i<9; i+=2){
                        count = i;
                        if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                            break;
                        }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0){
                            //如果不是每两个都相等的不行
                            break;
                        }else if(i <8 && cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 1 ){
                            //短路与，判断对是否连续
                            break;
                        }
                        count = i+2;
                    }
                    if (count == 10){
                        cardType = "连对(五对)";
                    }else {
                        //判断是否顺子
                        for (int i = 0; i < 9; i++) {
                            count = i;
                            if (cards.get(i).getNumber_label() > 14) {
                                break;
                            } else if (cards.get(i).getNumber_label() - cards.get(i + 1).getNumber_label() != 1) {
                                break;
                            }
                            count = i + 1;
                        }
                        if (count == 9) {
                            cardType = "顺子";
                        } else {
                            //不符合出牌规则
                            cardType = "不符合出牌规则";
                        }
                    }
                }
                break;
            case 11://十一张：顺子
                for (int i = 0; i<10; i++){
                    count = i;
                    if (cards.get(i).getNumber_label()>14){
                        break;
                    }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 1){
                        break;
                    }
                    count = i+1;
                }
                if (count == 10){
                    cardType = "顺子";
                }else {
                    //不符合出牌规则
                    cardType = "不符合出牌规则";
                }
                break;
            case 12://十二张：顺子，飞机(三个三带一)，四个三不带，连对(六个)
                //判断是否飞机
                for (firstIndex = 0;  firstIndex<4 ; firstIndex++) {
                    for (int i = firstIndex; i<firstIndex+7; i+=3){
                        count = i;
                        if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                            break;
                        }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0 ||
                                cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 0 ){
                            //如果不是每三个都相等的不行
                            break;
                        }else if(i < firstIndex+6 && cards.get(i).getNumber_label()-cards.get(i+3).getNumber_label() != 1 ){
                            //短路与，判断三张是否连续
                            break;
                        }
                        count = i+3;
                    }
                    if (count == firstIndex+9){
                        break;
                    }
                }
                if (firstIndex == 0){ //三个三带一还是四个三不带
                    lastcard = cards.get(11);
                    for (lastIndex = 10; lastIndex > 8; lastIndex--) {
                        if (comparator.compare(lastcard,cards.get(lastIndex))!=0){
                            break;
                        }
                    }
                    if(lastIndex == 8){
                        cardType = "四个三不带";
                    }else {
                        cardType = "飞机(三个三带一)";
                    }
                }else if(firstIndex > 0 && firstIndex < 4){  //
                    cardType = "飞机(三个三带一)";
                }else{
                    //判断是否连对(六对)
                    for (int i = 0; i<11; i+=2){
                        count = i;
                        if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                            break;
                        }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0){
                            //如果不是每两个都相等的不行
                            break;
                        }else if(i <10 && cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 1 ){
                            //短路与，判断对是否连续
                            break;
                        }
                        count = i+2;
                    }
                    if (count == 12){
                        cardType = "连对(六对)";
                    }else{
                        //判断是否顺子
                        for (int i = 0; i<11; i++){
                            count = i;
                            if (cards.get(i).getNumber_label()>14){
                                break;
                            }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 1){
                                break;
                            }
                            count = i+1;
                        }
                        if (count == 11){
                            cardType = "顺子";
                        }else {
                            //不符合出牌规则
                            cardType = "不符合出牌规则";
                        }
                    }
                }
                break;
            case 14://十四张：连对(七个)
                for (int i = 0; i<13; i+=2){
                    count = i;
                    if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                        break;
                    }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0){
                        //如果不是每两个都相等的不行
                        break;
                    }else if(i <12 && cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 1 ){
                        //短路与，判断对是否连续
                        break;
                    }
                    count = i+2;
                }
                if (count == 14){
                    cardType = "连对(七对)";
                }else{
                    //不符合出牌规则
                    cardType = "不符合出牌规则";
                }
                break;
            case 15://十五张：飞机(三个三带一对，五个三不带)
                for (firstIndex = 0;  firstIndex<7 ; firstIndex+=2) {
                    for (int i = firstIndex; i<firstIndex+7; i+=3){
                        count = i;
                        if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                            break;
                        }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0 ||
                                cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 0 ){
                            //如果不是每三个都相等的不行
                            break;
                        }else if(i < firstIndex+6 && cards.get(i).getNumber_label()-cards.get(i+3).getNumber_label() != 1 ){
                            //短路与，判断三张是否连续
                            break;
                        }
                        count = i+3;
                    }
                    if (count == firstIndex+9){
                        break;
                    }
                }
                if (firstIndex == 0){ //判断是三个三带一对还是五个三不带
                    firstcard = cards.get(9);
                    for (firstIndex = 10;firstIndex<12;firstIndex++){
                        if (comparator.compare(firstcard,cards.get(firstIndex))!=0){
                            break;
                        }
                    }
                    lastcard = cards.get(12);
                    for (lastIndex = 13;lastIndex<15;lastIndex++){
                        if (comparator.compare(lastcard,cards.get(lastIndex))!=0){
                            break;
                        }
                    }
                    if (firstIndex == 12 && lastIndex == 15 && comparator.compare(firstcard,lastcard) == 1){
                        cardType = "五个三不带";
                    }else{
                        if(comparator.compare(cards.get(9),cards.get(10)) == 0 &&
                           comparator.compare(cards.get(11),cards.get(12)) == 0 &&
                           comparator.compare(cards.get(13),cards.get(14)) == 0){
                            cardType = "飞机(三个三带一对)";
                        }else{
                            //不符合出牌规则
                            cardType = "不符合出牌规则";
                        }
                    }
                }else if(firstIndex == 2){  //判断是否三个三带一对
                    if(comparator.compare(cards.get(0),cards.get(1)) == 0 &&
                       comparator.compare(cards.get(11),cards.get(12)) == 0 &&
                       comparator.compare(cards.get(13),cards.get(14)) == 0){
                        cardType = "飞机(三个三带一对)";
                    }else{
                        //不符合出牌规则
                    }
                }else if(firstIndex == 4){  //判断是否三个三带一对
                    if(comparator.compare(cards.get(0),cards.get(1)) == 0 &&
                       comparator.compare(cards.get(2),cards.get(3)) == 0 &&
                       comparator.compare(cards.get(13),cards.get(14)) == 0){
                        cardType = "飞机(三个三带一对)";
                    }else{
                        //不符合出牌规则
                    }
                }else if(firstIndex == 6){  //判断是否三个三带一对
                    if(comparator.compare(cards.get(0),cards.get(1)) == 0 &&
                       comparator.compare(cards.get(2),cards.get(3)) == 0 &&
                       comparator.compare(cards.get(4),cards.get(5)) == 0){
                        cardType = "飞机(三个三带一对)";
                    }else{
                        //不符合出牌规则
                        cardType = "不符合出牌规则";
                    }
                }else{
                    //不符合出牌规则
                    cardType = "不符合出牌规则";
                }
                break;
            case 16://十六张：连对(八)，飞机(四个三带一)
                for (firstIndex = 0;  firstIndex<5 ; firstIndex++) {
                    for (int i = firstIndex; i<firstIndex+10; i+=3){
                        count = i;
                        if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                            break;
                        }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0 ||
                                cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 0 ){
                            //如果不是每三个都相等的不行
                            break;
                        }else if(i < firstIndex+9 && cards.get(i).getNumber_label()-cards.get(i+3).getNumber_label() != 1 ){
                            //短路与，判断三张是否连续
                            break;
                        }
                        count = i+3;
                    }
                    if (count == firstIndex+12){
                        break;
                    }
                }
                if (firstIndex < 5){ //飞机(四个三带一)
                    cardType = "飞机(四个三带一)";
                }else {
                    //判断是否连对
                    for (int i = 0; i<15; i+=2){
                        count = i;
                        if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                            break;
                        }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0){
                            //如果不是每两个都相等的不行
                            break;
                        }else if(i <14 && cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 1 ){
                            //短路与，判断对是否连续
                            break;
                        }
                        count = i+2;
                    }
                    if (count == 16){
                        cardType = "连对(八对)";
                    }else{
                        //不符合出牌规则
                        cardType = "不符合出牌规则";
                    }
                }
                break;
            case 18://十八张：连对(九个)，飞机(六个三不带)
                //判断是否六个三不带
                for (int i = 0; i<16; i+=3){
                    count = i;
                    if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                        break;
                    }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0 ||
                            cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 0 ){
                        //如果不是每三个都相等的不行
                        break;
                    }else if(i <15 && cards.get(i).getNumber_label()-cards.get(i+3).getNumber_label() != 1 ){
                        //短路与，判断三张是否连续
                        break;
                    }
                    count = i+3;
                }
                if (count == 18){
                    cardType = "六个三不带";
                }else{
                    //判断是否连对
                    for (int i = 0; i<17; i+=2){
                        count = i;
                        if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                            break;
                        }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0){
                            //如果不是每两个都相等的不行
                            break;
                        }else if(i <16 && cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 1 ){
                            //短路与，判断对是否连续
                            break;
                        }
                        count = i+2;
                    }
                    if (count == 18){
                        cardType = "连对(九对)";
                    }else{
                        //不符合出牌规则
                        cardType = "不符合出牌规则";
                    }
                }
                break;
            case 20://二十张：飞机(五个三带一)，飞机(四个三带二)，连对(十个)
                //判断是否飞机(五个三带一)
                for (firstIndex = 0;  firstIndex<6 ; firstIndex++) {
                    for (int i = firstIndex; i<firstIndex+13; i+=3){
                        count = i;
                        if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                            break;
                        }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0 ||
                                cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 0 ){
                            //如果不是每三个都相等的不行
                            break;
                        }else if(i < firstIndex+12 && cards.get(i).getNumber_label()-cards.get(i+3).getNumber_label() != 1 ){
                            //短路与，判断三张是否连续
                            break;
                        }
                        count = i+3;
                    }
                    if (count == firstIndex+15){
                        break;
                    }
                }
                if(firstIndex < 6){
                    cardType = "飞机(五个三带一)";
                }else{
                    //判断是否四个三带一对
                    for (firstIndex = 0;  firstIndex<9 ; firstIndex+=2) {
                        for (int i = firstIndex; i<firstIndex+10; i+=3){
                            count = i;
                            if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                                break;
                            }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0 ||
                                    cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 0 ){
                                //如果不是每三个都相等的不行
                                break;
                            }else if(i < firstIndex+8 && cards.get(i).getNumber_label()-cards.get(i+3).getNumber_label() != 1 ){
                                //短路与，判断三张是否连续
                                break;
                            }
                            count = i+3;
                        }
                        if (count == firstIndex+12){
                            break;
                        }
                    }
                    if (firstIndex == 0){
                        if(comparator.compare(cards.get(12),cards.get(13)) == 0 &&
                           comparator.compare(cards.get(14),cards.get(15)) == 0 &&
                           comparator.compare(cards.get(16),cards.get(17)) == 0 &&
                           comparator.compare(cards.get(18),cards.get(19)) == 0){
                            cardType = "飞机(四个三带一对)";
                        }else{
                            //不符合出牌规则
                            cardType = "不符合出牌规则";
                        }
                    }else if (firstIndex == 2){
                        if(comparator.compare(cards.get(0),cards.get(1)) == 0 &&
                           comparator.compare(cards.get(14),cards.get(15)) == 0 &&
                           comparator.compare(cards.get(16),cards.get(17)) == 0 &&
                           comparator.compare(cards.get(18),cards.get(19)) == 0){
                            cardType = "飞机(四个三带一对)";
                        }else{
                            //不符合出牌规则
                            cardType = "不符合出牌规则";
                        }
                    }else if (firstIndex == 4){
                        if(comparator.compare(cards.get(0),cards.get(1)) == 0 &&
                           comparator.compare(cards.get(2),cards.get(3)) == 0 &&
                           comparator.compare(cards.get(16),cards.get(17)) == 0 &&
                           comparator.compare(cards.get(18),cards.get(19)) == 0){
                            cardType = "飞机(四个三带一对)";
                        }else{
                            //不符合出牌规则
                            cardType = "不符合出牌规则";
                        }
                    }else if(firstIndex == 6){  //是否三个三带一对
                        if(comparator.compare(cards.get(0),cards.get(1)) == 0 &&
                           comparator.compare(cards.get(2),cards.get(3)) == 0 &&
                           comparator.compare(cards.get(4),cards.get(5)) == 0 &&
                           comparator.compare(cards.get(18),cards.get(19)) == 0){
                            cardType = "飞机(四个三带一对)";
                        }else{
                            //不符合出牌规则
                            cardType = "不符合出牌规则";
                        }
                    }else if(firstIndex == 8){  //是否三个三带一对
                        if(comparator.compare(cards.get(0),cards.get(1)) == 0 &&
                           comparator.compare(cards.get(2),cards.get(3)) == 0 &&
                           comparator.compare(cards.get(4),cards.get(5)) == 0 &&
                           comparator.compare(cards.get(6),cards.get(7)) == 0){
                            cardType = "飞机(四个三带一对)";
                        }else{
                            //不符合出牌规则
                            cardType = "不符合出牌规则";
                        }
                    }else{
                        //判断是否连对(十对)
                        for (int i = 0; i<19; i+=2){
                            count = i;
                            if (cards.get(i).getNumber_label()>14){ //如果不是'2'~'A'的都不行
                                break;
                            }else if(cards.get(i).getNumber_label()-cards.get(i+1).getNumber_label() != 0){
                                //如果不是每两个都相等的不行
                                break;
                            }else if(i <18 && cards.get(i).getNumber_label()-cards.get(i+2).getNumber_label() != 1 ){
                                //短路与，判断对是否连续
                                break;
                            }
                            count = i+2;
                        }
                        if (count == 20){
                            cardType = "连对(十对)";
                        }else{
                            //不符合出牌规则
                            cardType = "不符合出牌规则";
                        }
                    }
                }
                break;
            case 13://十三张：无
            case 17://十七张：无
            case 19://十九张：无
            default:
                //不符合出牌规则
                cardType = "不符合出牌规则";
                break;
        }
        return cardType;
}

    //比较两手牌的大小(比大小是为了出牌,所以返回一个boolean值)
    public static boolean compareCard(ArrayList<CardBox.Card> recentCards , ArrayList<CardBox.Card> nowCards) {
        boolean result = false;
        String type_1 = PaiZhuo.rule(recentCards);
        String type_2 = PaiZhuo.rule(nowCards);
        int size_1 = recentCards.size();
        int size_2 = nowCards.size();
        if ("王炸".equals(type_1)) {
            return false;
        }
        if ("王炸".equals(type_2)) {
            return true;
        }
        if ("炸弹".equals(type_1) && !"炸弹".equals(type_2)) {
            result = false;
        } else if (!"炸弹".equals(type_1) && "炸弹".equals(type_2)) {
            result = true;
        } else if ("炸弹".equals(type_1) && "炸弹".equals(type_2)) {
            //两个都是炸弹
            result = nowCards.get(0).getNumber_label() - recentCards.get(0).getNumber_label() > 0;
        } else {
            //没有炸弹，只有张数相同才能比较
            if (size_1 == size_2 && type_1.equals(type_2)) {
                int recentIndex = 0;
                int nowIndex = 0;
                switch (recentCards.size()) {
                    case 1:
                        result = compare(recentCards,nowCards,recentIndex,nowIndex);
                        break;
                    case 2:
                        result = compare(recentCards,nowCards,recentIndex,nowIndex);
                        break;
                    case 3:
                        result = compare(recentCards,nowCards,recentIndex,nowIndex);
                        break;
                    case 4:
                            //找recnetCards中三张中的一张索引
                            recentIndex = find(recentCards,3);
                            //找nowCards中三张中的一张索引
                            nowIndex = find(nowCards,3);
                            //作比较
                            result = compare(recentCards,nowCards,recentIndex,nowIndex);
                        break;
                    case 5:
                        if("三带一对".equals(type_1)){
                            //找recnetCards中三张中的一张索引
                            recentIndex = find(recentCards,3);
                            //找nowCards中三张中的一张索引
                            nowIndex = find(nowCards,3);
                            //作比较
                            result = compare(recentCards,nowCards,recentIndex,nowIndex);
                        }else if("顺子".equals(type_1))
                            result = compare(recentCards,nowCards,recentIndex,nowIndex);
                        break;
                    case 6:

                            if("四带二".equals(type_1)){
                                //找recnetCards中四张中的一张索引
                                recentIndex = find(recentCards,4);
                                //找nowCards中四张中的一张索引
                                nowIndex = find(nowCards,4);
                                //作比较
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if ("连对(三对)".equals(type_1)){
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("顺子".equals(type_1)){
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }
                        break;
                    case 7:
                        result = compare(recentCards,nowCards,recentIndex,nowIndex);
                        break;
                    case 8:
                            if("四带两对".equals(type_1)){
                                //找recnetCards中四张中的一张索引
                                recentIndex = find(recentCards,4);
                                //找nowCards中四张中的一张索引
                                nowIndex = find(nowCards,4);
                                //作比较
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("飞机(两个三带一)".equals(type_1)){
                                //找recnetCards中三张中的一张索引
                                recentIndex = find(recentCards,3);
                                //找nowCards中三张中的一张索引
                                nowIndex = find(nowCards,3);
                                //作比较
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("连对(四对)".equals(type_1)){
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("顺子".equals(type_1)){
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }
                        break;
                    case 9:
                            if("三个三不带".equals(type_1)){
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if ("顺子".equals(type_1)){
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }
                        break;
                    case 10:
                            if("飞机(两个三带一对)".equals(type_1)){
                                //找recnetCards中三张中的一张索引
                                recentIndex = find(recentCards,3);
                                //找nowCards中三张中的一张索引
                                nowIndex = find(nowCards,3);
                                //作比较
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("连对(五对)".equals(type_1)){
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("顺子".equals(type_1)){
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }
                        break;
                    case 11:
                        result = compare(recentCards,nowCards,recentIndex,nowIndex);
                        break;
                    case 12:
                            if("四个三不带".equals(type_1)){
                                //找recnetCards中三张中的一张索引
                                recentIndex = find(recentCards,3);
                                //找nowCards中三张中的一张索引
                                nowIndex = find(nowCards,3);
                                //作比较
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("飞机(三个三带一)".equals(type_1)){
                                //找recnetCards中三张中的一张索引
                                recentIndex = find(recentCards,3);
                                //找nowCards中三张中的一张索引
                                nowIndex = find(nowCards,3);
                                //作比较
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("连对(六对)".equals(type_1)){
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("顺子".equals(type_1)){
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }
                        break;
                    case 14:
                        result = compare(recentCards,nowCards,recentIndex,nowIndex);
                        break;
                    case 15:
                            if("五个三不带".equals(type_1)){
                                //找recnetCards中三张中的一张索引
                                recentIndex = find(recentCards,3);
                                //找nowCards中三张中的一张索引
                                nowIndex = find(nowCards,3);
                                //作比较
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("飞机(三个三带一对)".equals(type_2)){
                                //找recnetCards中三张中的一张索引
                                recentIndex = find(recentCards,3);
                                //找nowCards中三张中的一张索引
                                nowIndex = find(nowCards,3);
                                //作比较
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }
                        break;
                    case 16:
                            if("飞机(四个三带一)".equals(type_1)){
                                //找recnetCards中三张中的一张索引
                                recentIndex = find(recentCards,3);
                                //找nowCards中三张中的一张索引
                                nowIndex = find(nowCards,3);
                                //作比较
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("连对(八对)".equals(type_1)){
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }
                        break;
                    case 18:
                            if("六个三不带".equals(type_1)){
                                //找recnetCards中三张中的一张索引
                                recentIndex = find(recentCards,3);
                                //找nowCards中三张中的一张索引
                                nowIndex = find(nowCards,3);
                                //作比较
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("连对(九对)".equals(type_1)){
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }
                        break;
                    case 20:
                            if("飞机(五个三带一)".equals(type_1)){
                                //找recnetCards中三张中的一张索引
                                recentIndex = find(recentCards,3);
                                //找nowCards中三张中的一张索引
                                nowIndex = find(nowCards,3);
                                //作比较
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("飞机(四个三带一对)".equals(type_1)){
                                //找recnetCards中三张中的一张索引
                                recentIndex = find(recentCards,3);
                                //找nowCards中三张中的一张索引
                                nowIndex = find(nowCards,3);
                                //作比较
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }else if("连对(十对)".equals(type_1)){
                                result = compare(recentCards,nowCards,recentIndex,nowIndex);
                            }
                        break;
                }
            }
        }
    return result;
    }

    //在集合中找三张或四张中的一张在集合中的索引
    public static int find(ArrayList<CardBox.Card> cards, int number){
        int index = 0;
        int size = cards.size();
        if(number == 3){
            for (index = 0; index <size-2 ; index++) {
                if(cards.get(index).getNumber_label() == cards.get(index+1).getNumber_label() &&
                   cards.get(index).getNumber_label() == cards.get(index+2).getNumber_label()){
                    break;
                }
            }
        }else if (number == 4){
            for (index = 0; index <size-3 ; index++) {
                if(cards.get(index).getNumber_label() == cards.get(index+1).getNumber_label() &&
                   cards.get(index).getNumber_label() == cards.get(index+2).getNumber_label() &&
                   cards.get(index).getNumber_label() == cards.get(index+3).getNumber_label()){
                    break;
                }
            }
        }else {
            System.out.println("错误提示！");
            return -1;
        }
        return index;
    }

     //找到索引后比较两张牌的大小，返回true是nowCards大，返回false是recentCrads大
    public static boolean compare(ArrayList<CardBox.Card> recentCards,ArrayList<CardBox.Card> nowCards,
                                  int recentIndex, int nowIndex){
        return nowCards.get(nowIndex).getNumber_label() - recentCards.get(recentIndex).getNumber_label() > 0 ;
    }
    //用于解析用户的输入，输出一个正确的牌的集合
    public static ArrayList<CardBox.Card> parseString (String str){
        //输入格式：牌之间用 ','隔开，前后不能有逗号。
        //例："3♥,3♣,3♦,3♠",其中不包括双引号
        ArrayList<CardBox.Card> cards = new ArrayList<>();
        cards.clear();
        if (str.length() == 0) {
            return cards;
        }
        String[] strings = str.split(",");
        String number = "";
        String color = "";
        for (int i = 0; i < strings.length; i++) {
            String s = strings[i];
            if (s.length() == 2) {
                if ("大王".equals(s)) {

                    cards.add(new CardBox().new Card("大王", ""));
                } else if ("小王".equals(s)) {
                    cards.add(new CardBox().new Card("小王", ""));
                } else {
                    switch (s.charAt(0)) {
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case 'J':
                        case 'Q':
                        case 'K':
                        case 'A':
                            switch (s.charAt(1)) {
                                case '♠':
                                case '♥':
                                case '♣':
                                case '♦':
                                    number = Character.toString(s.charAt(0));
                                    color = Character.toString(s.charAt(1));
                                    cards.add(new CardBox().new Card(number, color));
                                    break;
                                default:
                                    //输入有误
                                    System.out.println("输入错误!,请以指定格式重新输入:");
                                    return null;
                            }
                            break;
                        default:
                            //输入有误
                            System.out.println("输入错误!,请以指定格式重新输入:");
                            return null;
                    }

                }
            } else if (s.length() == 3) {
                switch (Character.toString(s.charAt(0))+s.charAt(1)+s.charAt(2)){
                    case "10♠":
                    case "10♥":
                    case "10♣":
                    case "10♦":
                        number = Character.toString(s.charAt(0)) + Character.toString(s.charAt(1));
                        color = Character.toString(s.charAt(2));
                        cards.add(new CardBox().new Card(number, color));
                        break;
                    default:
                        //输入有误
                        System.out.println("输入错误!,请以指定格式重新输入:");
                        return null;
                }

            }else {
                //输入有误
                System.out.println("输入错误!,请以指定格式重新输入:");
                return null;
            }
        }
        Collections.sort(cards, (Comparator) (o1, o2) -> {
            CardBox.Card card1 = (CardBox.Card) o1;
            CardBox.Card card2 = (CardBox.Card) o2;
            return card1.getNumber_label() - card2.getNumber_label();
        });
        return cards;
    }
}
