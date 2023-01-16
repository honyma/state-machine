# statemachine状态机
<h1>1、简述</h1>
<p><span style="color: rgb(37,43,58);">状态机是状态模式的一种应用，相当于上下文角色的一个升级版。在工作流或订单系统、支付系统等各种系统中有大量使用，如各种工作流引擎，它几乎是状态机的子集和实现，封装状态的变化规则。状态机可以帮助开发者简化状态控制的开发过程，让状态机结构更加层次化。</span></p>
<p><span style="color: rgb(37,43,58);">常说的状态机是有限状态机FSM（Finite State Machine）。FSM指的是有有限个状态（一般是一个状态变量的值），这个机器同时能够从外部接收信号和信息输入，机器在接收到外部输入的信号后会综合考虑当前自己的状态和用户输入的信息，然后机器做出动作：跳转到另一个状态。</span></p>
<p><span style="color: rgb(37,43,58);">状态机是有限状态自动机的简称，是现实事物运行规则抽象而成的一个数学模型，通过<span style="color: rgb(37,43,58);">统一管理状态</span>和控制状态之间的流转，维护了系统内状态的生命周期。<br /></span></p>
<p><br /></p>
<h1>2、背景</h1>
<ul>
<li>支付系统、订单系统等交易系统状态众多，而且每个状态分散在各个地方，状态流转很复杂，代码逻辑就会很复杂，可读性低，导致后期维护困难。改动风险更大，所以需要有一个专门定义订单状态及状态变更的地方去处理。</li>
<li>业界在电商平台、物流状态、购物订单状态流转大多使用状态机处理。</li>
<li>遍历一个 if-else 结构（或者更糟的是，多个这样的结构），检查是否设置了特定的标志或枚举，然后在标志和枚举的某些组合存在或不存在时进一步判断要做什么,说明你可以把这些系统和功能进行抽象化，您已经在尝试实施状态机了。<br /><br /></li></ul>
<h1>3、状态机五大要素</h1>
<ul>
<li>State（状态）就是一个系统在其生命周期中某一个时刻的运行情况，此时，系统会执行一些操作，或者等待一些外部输入。并且，在当前形态下，可能会有不同的行为和属性。一个状态机至少要包含两个状态。</li>
<li>Guard（条件）状态机对外部消息进行响应时，除了需要判断当前的状态，还需要判断跟这个状态相关的一些条件是否成立。这种判断称为 Guard（条件）。Guard 通过允许或者禁止某些操作来影响状态机的行为。</li>
<li>Event（事件）就是在一定的时间和空间上发生的对系统有意义的事情，事件通常会引起状态的变迁，促使状态机从一种状态切换到另一种状态。事件就是执行某个操作的触发条件或者口令。</li>
<li>Action（动作）当一个事件（Event）被状态机系统分发的时候，状态机用 动作（Action）来进行响应，用于实现当前节点对应的业务逻辑处理。事件发生以后要执行某些动作。比如修改一下变量的值、进行输入输出、产生另外一个 Event 或者迁移到另外一个状态等。</li>
<li>Transition（迁移）从一个状态切换到另一个状态被称为 Transition（迁移）。引起状态迁移的事件被称为触发事件（triggering event），或者被简称为触发（trigger）。</li></ul>
<p><br /></p>
<h1>4、框架选型</h1>
<table class="relative-table" style="width: 98.6835%;"><colgroup><col style="width: 4.44939%;" /><col style="width: 24.416%;" /><col style="width: 23.693%;" /><col style="width: 23.9155%;" /><col style="width: 23.5261%;" /></colgroup>
<tbody>
<tr>
<th>功能指标</th>
<th>spring statemachine</th>
<th>stateless4j</th>
<th>cola statemachine</th>
<th colspan="1">squirrel foundation</th></tr>
<tr>
<td><span>核心模型</span></td>
<td>
<p>StateMachineStateConfigurer： 定义状态的entry exit action</p>
<p>StateMachineTransitionConfigurer： 定义状态变更触发事件、状态转移事件</p>
<p>StateMachineConfigurationConfigurer 状态机系统配置，包括action执行器、listener(事件监听器)等</p>
<p>StateMachineListener 事件监听器(通过Spring的event机制实现)，stateEntered、stateExited等事件，借助listener可以trace</p>
<p>StateMachineInterceptor 状态拦截器，内部的PersistingStateChangeInterceptor(状态持久化)等都是基于这个扩展协议生效的</p>
<p>StateMachine 状态机实例，spring statemachine支持单例、工厂模式两种方式创建，每个statemachine有一个独有的machineId用于标识machine实例</p></td>
<td>
<p>模型比较简单，源自C#的FSM版本的实现。</p>
<p>主要结构分4层：</p>
<p>StateRepresentation：记录每个状态的entry、exit、action、引起该状态变更的事件。</p>
<p>StateMachineConfig：配置类，创建状态机实例，维护了State-&gt;StateRepresentation的map结构。通过特定State找到StateRepresentation，执行相应的entry、exit等操作。permitDynamic支持传参，对相同事件，不同参数，变更到不同目的状态。</p>
<p>StateConfiguration: 配置实例，StateMachineConfig.configure创建</p>
<p>StateMachine：状态机实例，不能共享，保存了当前状态。随用随new，实例创建开销小。</p></td>
<td>
<p>Alibaba出品，无状态的状态机</p>
<p>StateMachineBuilderFactory：工厂类<br />StateMachineBuilder：状态机实例构造类，定义事件触发、状态变更、<br />状态迁移、及action</p>
<p>StateMachine：状态机实例，由StateMachineBuilder创建，可以指定machineId。<br />也可以根据machineId获取定义好的状态机实例。</p>
<p>支持传参StateContext，不同参数变更到不同目的状态</p>
<p>状态机实例不保存初始状态、当前状态，仅对给出得当前state，触发事件，action，condition，做出判断，流转至目的状态即可，可以复用。</p>
<p>构建模型采用访问者设计模式，大部分设计与squirrel-foundation类似</p></td>
<td colspan="1">
<p>StateMachineBuilderFactory: StateMachineBuilder工厂类</p>
<p>StateMachineBuilder：状态机实例构造器，可复用(不能再次更改任何元素，只限于创建相同状态定义的不同实例)。定义状态变更、事件触发、action等</p>
<p>StateMachineListener：全局事件监听，包含begin、complete、exception类型事件监听。</p>
<p>StateMachine：由StateMachineBuilder创建，轻量级，不能共享。支持afterTransitionCausedException、beforeTransitionBegin、beforeActionInvoked、afterActionInvoked等事件的自定义全局处理流程，作用类似于spring statemachine中的inteceptor。支持传参StateContext，不同参数变更到不同目的状态</p></td></tr>
<tr>
<td colspan="1">优势</td>
<td colspan="1">
<p>spring家族，未来肯定会更加完善</p>
<p>支持事件监听、拦截、持久化，状态链路可追踪。</p>
<p>事件接收与状态迁移任务采用队列存储，逻辑分层处理。</p>Spring IOC 集成将 bean 与状态机相关联。</td>
<td colspan="1">
<p>实现简单、很轻量、实例创建开销小，不需要侵入到spring框架。</p>
<p>核心代码少</p>
<p>实现了状态机的基本用法，包括状态、事件、变更、action等</p></td>
<td colspan="1">
<p>唯一无状态的状态机，不保存当前状态，仅仅提供当前状态、条件，输出目的状态、触发entry、exit、action。性能损耗最小</p>
<p>状态机随用随new，开销极小，核心代码仅仅几个类，足够轻量简单</p>
<p>实现了状态机的基本用法，包括状态、事件、变更、condition、action</p></td>
<td colspan="1">
<p>状态变更、事件触发、模型设计非常清晰，相比spring-statemachine轻量</p>
<p>文档详细、丰富的测试用例</p>
<p>事件处理与产生分离，使用deque交互，通过这种方式可以支持异步，采用生产-消费的方式。</p>
<p>提供了自定义扩展机制，能够方便的实现状态持久化以及状态trace等功能。</p>
<p><span style="color: rgb(33,37,41);">提供了DotVisitor、SCXMLVisitor两种实现方式用于生成状态机描述文件</span></p></td></tr>
<tr>
<td colspan="1">劣势</td>
<td colspan="1">
<p>状态机保存了当前状态，单例方式不适用，状态机的实例非线程安全的，所以在每一次状态机在接受请求的时候，都不得不重新 build 一个新的状态机实例。</p>
<p>状态机实例较重，在官方文档推荐注解的形式注入状态机，这样难以随用随new，侵入到框架</p></td>
<td colspan="1">
<p>不包含状态转移时触发事件transition action</p>
<p>不支持状态转移后的callback、listener，持久化扩展困难</p>
<p>同步模型，事件触发及处理在同一个线程处理</p>
<p>官网文档介绍不全，测试用例不详细</p>
<p>condition不支持传参，实际上是假的动态guard</p></td>
<td colspan="1">
<p>不支持状态的嵌套（substate），状态的并行（parallel，fork，join）、子状态机等高级用法，仅支持基础功能</p>
<p>不支持持久化、监听、拦截处理</p>
<p>发布不久，可能还没经过业务落地考验</p></td>
<td colspan="1">
<p>注册事件处理方法时通过传入String类型方法名来实现，框架在处理时从状态机实现类中去找这些方法，通过动态的方式调用。可能存在风险，编译期间难以发现错误。</p>
<p>squirrel存在依赖冲突问题需要解决。</p>模型相比较spring statemachine较为简单，实现复杂的多层状态模型需要在此基础上开发实现</td></tr>
<tr>
<td colspan="1">文档链接</td>
<td colspan="1"><a rel="nofollow" href="https://projects.spring.io/spring-statemachine/#quick-start">https://projects.spring.io/spring-statemachine/#quick-start</a></td>
<td colspan="1"><a rel="nofollow" href="https://github.com/stateless4j/stateless4j">https://github.com/stateless4j/stateless4j</a></td>
<td colspan="1"><a rel="nofollow" href="https://github.com/alibaba/COLA">https://github.com/alibaba/COLA</a></td>
<td colspan="1"><a href="https://github.com/hekailiang/squirrel" rel="nofollow">https://github.com/hekailiang/squirrel</a></td></tr>
<tr>
<td colspan="1">发布时间</td>
<td colspan="1"><span style="color: rgb(88,96,105);">19 Mar 2019</span></td>
<td colspan="1"><span style="color: rgb(88,96,105);">5 Apr 2014</span></td>
<td colspan="1"><span style="color: rgb(88,96,105);">30 May 2019</span></td>
<td colspan="1"><span style="color: rgb(88,96,105);">19 Feb 2014</span></td></tr></tbody></table>
<p><br /></p>

