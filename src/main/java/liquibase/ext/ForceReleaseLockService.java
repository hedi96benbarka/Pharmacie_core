/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package liquibase.ext;

import liquibase.exception.DatabaseException;
import liquibase.exception.LockException;
import liquibase.lockservice.StandardLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ForceReleaseLockService extends StandardLockService {

    private final Logger log = LoggerFactory.getLogger(ForceReleaseLockService.class);

    @Override
    public int getPriority() {
        return super.getPriority() + 1;
    }

    @Override
    public void waitForLock() throws LockException {
        try {
            log.info("force to release lock");
            super.forceReleaseLock();
        } catch (DatabaseException e) {
            throw new LockException("Could not enforce getting the lock.", e);
        }
        super.waitForLock();
    }
}
