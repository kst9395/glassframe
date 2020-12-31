package com.ethopia.tradecabinet.security;

import com.google.inject.Inject;
import io.reactivex.Maybe;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.WorkerExecutor;

import java.nio.ByteBuffer;

public class RxPasswordEncoderImpl implements RxPasswordEncoder {

    private WorkerExecutor workerExecutor;
    private PasswordEncoder delegate;

    private static final Logger logger = LoggerFactory.getLogger(RxPasswordEncoderImpl.class.getName());

    @Inject
    public void setWorkerExecutor(WorkerExecutor workerExecutor) {
        this.workerExecutor = workerExecutor;
    }

    @Inject
    public void setDelegate(PasswordEncoder delegate) {
        this.delegate = delegate;
    }

    private String toHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            int decimal = (int) aByte & 0xff;               // bytes widen to int, need mask, prevent sign extension
            // get last 8 bits
            String hex = Integer.toHexString(decimal);
            if (hex.length() % 2 == 1) {                    // if half hex, pad with zero, e.g \t
                hex = "0" + hex;
            }
            result.append(hex);
        }
        return result.toString();
    }

    private byte[] toBytes(String string) {
        byte[] val = new byte[string.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(string.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }

        return val;
    }

    @Override
    public Maybe<String> encodePassword(String password) {
        return
                generateSalt(32).flatMap(byteBuffer -> {
                    byte[] salt = byteBuffer.array();
                    return workerExecutor.rxExecuteBlocking(promise -> {
                        try {
                            byte[] hashBytes = delegate.encode(password.toCharArray(), salt, PasswordEncoder.PBKDF2_ITERATIONS, PasswordEncoder.KEY_LENGTH);
                            StringBuffer fullPassword = new StringBuffer();
                            fullPassword.append(PasswordEncoder.PBKDF2_ITERATIONS).append(PasswordEncoder.DELIMITER).append(toHex(hashBytes)).append(PasswordEncoder.DELIMITER).append(toHex(salt));
                            promise.complete(fullPassword.toString());
                        } catch (Exception e) {
                            promise.fail(e);
                        }
                    }, false);
                });

    }

    private Maybe<ByteBuffer> generateSalt(int size) {
        return workerExecutor.rxExecuteBlocking(promise -> {
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                byteBuffer.put(delegate.generateSalt(size));
                byteBuffer.flip();
                promise.complete(byteBuffer);
            } catch (Exception e) {
                promise.fail(e);
            }
        }, false);
    }

    @Override
    public Maybe<Boolean> matchPassword(String password, String goodHash) {
        return workerExecutor.rxExecuteBlocking(promise -> {
            try {
                String[] parts = goodHash.split(PasswordEncoder.DELIMITER);
                if (parts.length != 3) {
                    promise.fail(new IllegalStateException("Invalid hash, expected 3 parts actual " + parts.length + " parts"));
                    return;
                }
                int iterationCount = Integer.parseInt(parts[0]);
                byte[] salt = toBytes(parts[2]);
                String goodHashPart = parts[1];
                String inputHash = toHex(delegate.encode(password.toCharArray(), salt, iterationCount, PasswordEncoder.KEY_LENGTH));
                promise.complete(goodHashPart.equals(inputHash));

            } catch (Exception e) {
                promise.fail(e);
            }
        }, false);
    }
}
