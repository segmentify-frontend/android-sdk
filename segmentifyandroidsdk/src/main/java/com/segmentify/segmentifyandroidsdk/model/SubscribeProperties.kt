package com.segmentify.segmentifyandroidsdk.model

sealed class SubscribeProperties(val channel: Channel) {

    data class AppPush(
        var fcmToken: String? = null,
    ) : SubscribeProperties(Channel.APP_PUSH)

    data class Email(
        var email: String,
        var purpose: List<Purpose>
    ) : SubscribeProperties(Channel.EMAIL)

    data class Whatsapp(
        var phone: String
        var purpose: List<Purpose>
    ) : SubscribeProperties(Channel.WHATSAPP)

    data class Sms(
        var phone: String
        var purpose: List<Purpose>
    ) : SubscribeProperties(Channel.SMS)

    data class Call(
        var phone: String
        var purpose: List<Purpose>
    ) : SubscribeProperties(Channel.CALL)
}
