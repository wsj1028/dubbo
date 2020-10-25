/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.demo.provider;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.demo.DemoService;

import java.util.concurrent.CountDownLatch;

public class Application {
    public static void main(String[] args) throws Exception {
        if (true) {
            startWithExport();
        } else {
            startWithBootstrap();
        }
    }

    private static boolean isClassic(String[] args) {
        return args.length > 0 && "classic".equalsIgnoreCase(args[0]);
    }

    private static void startWithBootstrap() {

        // 创建一个ServiceConfig instance 泛型参数是业务接口实现类
        // DemoServiceImpl
        ServiceConfig<DemoServiceImpl> service = new ServiceConfig<>();
        // 指定业务接口
        service.setInterface(DemoService.class);
        // 指定业务实接口的实现, 由该对象来处理Consumer的request
        service.setRef(new DemoServiceImpl());
        // 获取单例对象  dubboBootstrap 启动对象
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        System.out.println("bootstrap provider = " + bootstrap);
        // 设置应用信息 ApplicationConfig  zk 地址 以及服务实例ServiceConfig
        bootstrap.application(new ApplicationConfig("dubbo-demo-api-provider"))
                .registry(new RegistryConfig("zookeeper://106.75.249.240:2181"))
                .service(service)
                .start()
                .await();
    }

    private static void startWithExport() throws InterruptedException {

        // 创建一个ServiceConfig instance 泛型参数是业务接口实现类
        // DemoServiceImpl
        ServiceConfig<DemoServiceImpl> service = new ServiceConfig<>();
        // 指定业务接口
        service.setInterface(DemoService.class);
        // 指定业务实接口的实现, 由该对象来处理Consumer的request
        service.setRef(new DemoServiceImpl());
        service.setApplication(new ApplicationConfig("dubbo-demo-api-provider"));
        service.setRegistry(new RegistryConfig("zookeeper://106.75.249.240:2181"));
        service.export();

        System.out.println("dubbo service started");
        new CountDownLatch(1).await();
        System.out.println("dubbo service stopde");
    }
}
