![l_logo](https://github.com/linheimx/LChart/blob/master/art/l_logo.png)

# LChart
## 简介
这是一个折线图，它提供了几个非常实用的功能，并且非常简单，易于使用。

## 功能特色
1. 支持缩放，拖拽
2. 支持多条数据线
3. 支持上帝视角：预览图谱
4. 支持点击数据点时的十字高亮与数值提示
5. 支持高亮点的左右移动
6. 支持添加预警线
7. 支持实时数据的添加


## 效果展示
![basic](https://github.com/linheimx/LChart/blob/master/art/l_basic.png)  ![warn](https://github.com/linheimx/LChart/blob/master/art/l_warn.png)
![multi](https://github.com/linheimx/LChart/blob/master/art/l_multi.png)  ![func](https://github.com/linheimx/LChart/blob/master/art/l_func.png)
![god](https://github.com/linheimx/LChart/blob/master/art/l_god.png)  ![realtime](https://github.com/linheimx/LChart/blob/master/art/l_realtime.png)


## 基本使用
添加依赖
```
compile 'com.linheimx.library:lchart:1.0.0'
```
给LineChart添加数据

```
// step1: 设置x,y轴
XAxis xAxis = lineChart.get_XAxis();
xAxis.set_unit("单位：s");
xAxis.set_ValueAdapter(new DefaultValueAdapter(1));

YAxis yAxis = lineChart.get_YAxis();
yAxis.set_unit("单位：m");
yAxis.set_ValueAdapter(new DefaultValueAdapter(3));// 默认精度到小数点后2位,现在修改为3位精度

// step2: 为一条数据线添加数据
Line line = new Line();
List<Entry> list = new ArrayList<>();
list.add(new Entry(1, 5));
list.add(new Entry(2, 4));
list.add(new Entry(3, 2));
list.add(new Entry(4, 3));
list.add(new Entry(10, 8));
line.setEntries(list);

// step3: 将数据放到 lineChart上
Lines lines = new Lines();
lines.addLine(line);

lineChart.setLines(lines);
```

## 项目分析
博客地址：http://www.jianshu.com/p/d03ff80ad508

欢迎反馈问题，我会尽力来解决的，希望我们会做的更好 : )