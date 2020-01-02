package org.itrunner.heroes.scheduling;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HelloSpring {

    @Scheduled(cron = "0 */1 * * * *")
    @SchedulerLock(name = "helloSpringScheduler", lockAtLeastFor = "PT30S", lockAtMostFor = "PT3M")
    public void sayHello() {
        log.info("Hello Spring Scheduler");
    }

}
