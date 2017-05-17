package com.example.android.asymmetricfingerprintdialog.server;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 의사 벡엔드 구현 
 */
public class StoreBackendImpl implements StoreBackend {

    private final Map<String, PublicKey> mPublicKeys = new HashMap<>();
    private final Set<Transaction> mReceivedTransactions = new HashSet<>();

    /**
     * 구매 트랜잭션 검증 메소드 
     */
    @Override
    public boolean verify(Transaction transaction, byte[] transactionSignature) {
        try {
            if (mReceivedTransactions.contains(transaction)) {
                // Replay 공격되지 않았는지 같은 nonce를 포함하는지 검증한다
                return false;
            }
            mReceivedTransactions.add(transaction);
            PublicKey publicKey = mPublicKeys.get(transaction.getUserId());
            Signature verificationFunction = Signature.getInstance("SHA256withECDSA");
            verificationFunction.initVerify(publicKey);
            verificationFunction.update(transaction.toByteArray());
            if (verificationFunction.verify(transactionSignature)) {
                // 사용자에게 연결된 공개키로 구매 트랜잭션을 검증한다. 
                return true;
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            // 검증에 문제가 일어난 경우, 실제 케이스에서는 클라이언트 사용자에게 메시지를 보낼 필요가 있다. 
        }
        return false;
    }

    @Override
    public boolean verify(Transaction transaction, String password) {
        // 예제 이므로, 패스워드에 의한 검증은 반드시 통과되어 있습니다. 
        return true;
    }

    @Override
    public boolean enroll(String userId, String password, PublicKey publicKey) {
        if (publicKey != null) {
            mPublicKeys.put(userId, publicKey);
        }
        // 이 예에서는 순수하게 클라이언트 쪽에서 계정의 ID와 패스워드가 제공되지만
        // 사용자 관리 방법은 앱과 서비스에 적절한 형태로 검토해야 합니다. 
        return true;
    }
}
