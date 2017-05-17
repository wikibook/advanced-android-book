package com.example.android.asymmetricfingerprintdialog.server;

import java.security.PublicKey;

/**
 * 백엔드 인터페이스 정의 
 */
public interface StoreBackend {

    /**
     * 클라이언트로부터 보내진 구매 트랜잭션이 사용자 ID에 연결된 비밀키로 서명됐는지 검증합니다.
     *
     * @param transaction          the contents of the purchase transaction, its contents are
     *                             signed
     *                             by the
     *                             private key in the client side.
     * @param transactionSignature the signature of the transaction's contents.
     * @return true if the signedSignature was verified, false otherwise. If this method returns
     * true, the server can consider the transaction is successful.
     */
    boolean verify(Transaction transaction, byte[] transactionSignature);

    /**
     * 클라이언트로부터 보내진 구매 트랜잭션을 패스워드로 검증합니다.
     *
     * @param transaction the contents of the purchase transaction, its contents are signed by the
     *                    private key in the client side.
     * @param password    the password for the user associated with the {@code transaction}.
     * @return true if the password is verified.
     */
    boolean verify(Transaction transaction, String password);

    /**
     * 사용자에게 연결된 공개키를 등록합니다.
     *
     * @param userId    the unique ID of the user within the app including server side
     *                  implementation
     * @param password  the password for the user for the server side
     * @param publicKey the public key object to verify the signature from the user
     * @return true if the enrollment was successful, false otherwise
     */
    boolean enroll(String userId, String password, PublicKey publicKey);
}
