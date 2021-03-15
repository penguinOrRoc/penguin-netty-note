#Netty模型
1.Netty抽象出两组线程池
    BossGroup专门负责接受客户端请求    
    WorkerGroup专门负责网络的读写     
2.BossGroup和WorkerGroup的类型都是NioEventLoopGroup
3.NioEventLoopGroup相当于事件循环组
    事件循环组中含有多个事件循环，每一个事件循环都是NioEventGroup
4.NioEventLoop表示一个不断循环的执行处理任务的线程
    每一个NioEventLoop都有一个selector，用于监听绑定在其上的socket的网络通讯
5.NioEventLoopGroup可以有多个线程/NioEventLoop
6.Boss NioEventLoop执行包含3步
    6.1轮询Accept事件
    6.2处理Accept事件，与client建立连接，生成NioScoketChannel，并将其注册到某个Worker NioEventLoop的selector上
    6.3处理任务队列的任务，即runAllTasks
7.Worker NioEventLoop循环执行的步骤
    7.1轮询read 、 write事件（连接事件由Boss NioEventLoop处理）
    7.2处理IO事件（read、write事件），在对应的NioSocketChannel进行处理
    7.3处理任务队列的任务，即runAllTasks
8.每一个Worker NIOEventLoop处理业务时，会使用pipeline（管道）
    pipeline包含了channel，即通过pipeline可以获取到对应的通道
    同样，也可通过channel获取到对应的pipeline

#Netty模型任务队列
应用场景
1.用户程序自定义的普通任务
2.用户自定义定时任务
3.非当前Reactor线程调用Channel的各种方法，
    如推送系统的业务线程，根据用户的标识，找到对应的Channel引用，然后调用Write类方法向该用户推送消息。最终Write会提交到任务队列中被异步消费。
    