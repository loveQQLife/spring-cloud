package com.kreken.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

public class UserServiceCommand extends HystrixCommand<String> {

    private final String commandName;

    public UserServiceCommand(String commandName) {
        super(HystrixCommandGroupKey.Factory.asKey("group"));
        this.commandName = commandName;
    }

    @Override
    protected String run() throws Exception {
        return commandName;
    }

    public static class UnitTest{

       // @Test
        public void testSynchronized(){
            String str = new UserServiceCommand("syn").execute();
            System.out.println(str);
        }

      //  @Test
        public void testAsynchronous() throws Exception {
          String str = new UserServiceCommand("asy").queue().get();
            System.out.println(str);
        }

    //    @Test
        public void testObservable() throws Exception{
            Observable<String> stringObservable = new UserServiceCommand("observer").observe();

            //non-blocking
            stringObservable.subscribe(new Observer<String>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void onNext(String s) {
                    System.out.println("Observer --onNext: "+ s);
                }
            });
            //non-blocking
            stringObservable.subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    System.out.println("Action1--onNext: "+ s);
                }
            });

            //non-blocking and java 8 without exception
            stringObservable.subscribe((v)->{
                System.out.println("non-exception-lambada onNext: "+ v);
            });

            stringObservable.subscribe(
                    (v)->{
                        System.out.println("exception-lambada onNext:"+v);
                    },(e)->{
                        e.printStackTrace();
                    }
            );

        }
    }
}
