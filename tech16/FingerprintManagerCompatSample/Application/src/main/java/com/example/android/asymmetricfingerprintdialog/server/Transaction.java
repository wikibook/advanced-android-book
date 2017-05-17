package com.example.android.asymmetricfingerprintdialog.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * 구매 트랜잭션을 표현하는 엔티티 클래스입니다.
 */
public class Transaction {

    /** 구매 아이템의 고유 ID */
    private final Long mItemId;

    /** 트랜잭션을 생성한 사용자의 고유 ID */
    private final String mUserId;

    /**
     * 같은 nonce가 Replay 공격으로 재이용되지 않게 비밀키로 서명되고 서버에서 검증된 nonce값 
     */
    private final Long mClientNonce;

    public Transaction(String userId, long itemId, long clientNonce) {
        mItemId = itemId;
        mUserId = userId;
        mClientNonce = clientNonce;
    }

    public String getUserId() {
        return mUserId;
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeLong(mItemId);
            dataOutputStream.writeUTF(mUserId);
            dataOutputStream.writeLong(mClientNonce);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
            } catch (IOException ignore) {
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException ignore) {
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Transaction that = (Transaction) o;
        return Objects.equals(mItemId, that.mItemId) && Objects.equals(mUserId, that.mUserId) &&
                Objects.equals(mClientNonce, that.mClientNonce);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mItemId, mUserId, mClientNonce);
    }
}
