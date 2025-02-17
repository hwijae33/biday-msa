package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import shop.biday.model.document.AccountDocument;
import shop.biday.model.domain.AccountModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.enums.Role;
import shop.biday.model.repository.MAccountRepository;
import shop.biday.model.repository.MUserRepository;
import shop.biday.service.AccountService;
import shop.biday.utils.UserInfoUtils;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final MAccountRepository accountRepository;
    private final MUserRepository userRepository;
    private final UserInfoUtils userInfoUtils;

    @Override
    public Mono<AccountDocument> findByUserId(String userInfoHeader) {

        UserInfoModel userInfo = userInfoUtils.extractUserInfo(userInfoHeader);

        return accountRepository.findByUserId(userInfo.getUserId());
    }

//    @Override
//    public Mono<AccountDocument> save(AccountModel accountModel) {
//        return userRepository.findById(accountModel.getUserId())
//                .flatMap(user -> {
//                    if (accountModel.getBankRspCode().equals("000")) {
//                        return accountRepository.findByUserId(accountModel.getUserId())
//                                .flatMap(accountDocument -> {
//                                    accountDocument.setBankTranId(accountModel.getBankTranId());
//                                    accountDocument.setBankCode(accountModel.getBankCode());
//                                    accountDocument.setBankName(accountModel.getBankName());
//                                    accountDocument.setAccountNumber(accountModel.getAccountNum());
//                                    accountDocument.setAccountName(accountModel.getAccountName());
//                                    accountDocument.setBankRspCode(accountModel.getBankRspCode());
//                                    accountDocument.setBankTranDate(accountModel.getBankTranDate());
//                                    return accountRepository.save(accountDocument)
//                                            .then(Mono.just(accountDocument));
//                                })
//                                .switchIfEmpty(Mono.defer(() -> {
//                                    AccountDocument accountDocument = AccountDocument.builder()
//                                            .userId(accountModel.getUserId())
//                                            .bankTranId(accountModel.getBankTranId())
//                                            .bankCode(accountModel.getBankCode())
//                                            .bankName(accountModel.getBankName())
//                                            .accountNumber(accountModel.getAccountNum())
//                                            .accountName(accountModel.getAccountName())
//                                            .bankRspCode(accountModel.getBankRspCode())
//                                            .bankTranDate(accountModel.getBankTranDate())
//                                            .build();
//                                    return accountRepository.save(accountDocument)
//                                            .flatMap(saveAccount -> {
//                                                user.setRole(Collections.singletonList(Role.ROLE_SELLER));
//                                                return userRepository.save(user)
//                                                        .then(Mono.just(saveAccount));
//                                            });
//                                }));
//                    } else {
//                        return Mono.error(new RuntimeException("유효하지 않은 은행 응답 코드입니다. "));
//                    }
//                });
//    }
public Mono<AccountDocument> save(AccountModel accountModel) {
    if (!accountModel.getBankRspCode().equals("000")) {
        return Mono.error(new RuntimeException("유효하지 않은 은행 응답 코드입니다."));
    }

    return accountRepository.findByUserId(accountModel.getUserId())
            .flatMap(existingAccount -> {
                // 기존 계좌 정보 업데이트
                existingAccount.setBankTranId(accountModel.getBankTranId());
                existingAccount.setBankCode(accountModel.getBankCode());
                existingAccount.setBankName(accountModel.getBankName());
                existingAccount.setAccountNumber(accountModel.getAccountNum());
                existingAccount.setAccountName(accountModel.getAccountName());
                existingAccount.setBankRspCode(accountModel.getBankRspCode());
                existingAccount.setBankTranDate(accountModel.getBankTranDate());

                return accountRepository.save(existingAccount);
            })
            .switchIfEmpty(Mono.defer(() -> {
                // 기존 계좌가 없을 경우 새로 생성
                AccountDocument newAccount = AccountDocument.builder()
                        .userId(accountModel.getUserId())
                        .bankTranId(accountModel.getBankTranId())
                        .bankCode(accountModel.getBankCode())
                        .bankName(accountModel.getBankName())
                        .accountNumber(accountModel.getAccountNum())
                        .accountName(accountModel.getAccountName())
                        .bankRspCode(accountModel.getBankRspCode())
                        .bankTranDate(accountModel.getBankTranDate())
                        .build();

                return accountRepository.save(newAccount);
            }))
            .flatMap(savedAccount -> {
                // 유저 역할 업데이트
                return userRepository.findById(accountModel.getUserId())
                        .flatMap(user -> {
                            user.setRole(Collections.singletonList(Role.ROLE_SELLER));
                            return userRepository.save(user)
                                    .then(Mono.just(savedAccount));
                        });
            });
}


}
