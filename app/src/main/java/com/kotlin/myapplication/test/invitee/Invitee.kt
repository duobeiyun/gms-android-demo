package com.duobeiyun.generamessagedemo.test.invitee

import android.content.Context
import android.util.Log
import com.duobeiyun.generamessagesdk.ErrorInfo
import com.duobeiyun.generamessagesdk.ResultCallback
import com.duobeiyun.generamessagesdk.client.GmsClient
import com.duobeiyun.generamessagesdk.invite.GmsInvitationEventListener
import com.duobeiyun.generamessagesdk.invite.Invitation
import com.duobeiyun.generamessagesdk.invite.InvitationFailureReason
import com.duobeiyun.generamessagesdk.invite.InvitationManager

/*
 * Author: cqr
 * Version: V100R001C01
 * Create: 2020/6/22 17:16
 * Changes (from 2020/6/22)
 */

class Invitee(var context: Context, var instance: GmsClient) {
    private var manager: InvitationManager? = null
    private var Tag = "Invite 接收方----------"
    private var index = 0
    fun beginTest() {
        manager = instance?.getRtmCallManager()
        setTestListener();
    }

    fun testRefuse(invitation2: Invitation) {
        manager?.refuse(invitation2!!, object : ResultCallback<Invitation> {
            override fun onSuccess(responseInfo: Invitation?) {
                log(responseInfo!!, "refuse");
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                Log.e(
                    "MainActivity",
                    Tag + " refuse onFailure  ${errorInfo}--$invitation2"
                )
            }

        })
    }

    fun testAccept(invitation2: Invitation) {
        manager?.accept(invitation2!!, object : ResultCallback<Invitation> {
            override fun onSuccess(responseInfo: Invitation?) {
                log(responseInfo!!, "accept");
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                Log.e(
                    "MainActivity",
                    Tag + " accept onFailure  ${errorInfo}--$invitation2"
                )
            }

        })
    }

    fun testcancel(invitation: Invitation) {
        manager?.cancel(invitation!!, object : ResultCallback<Invitation> {
            override fun onSuccess(responseInfo: Invitation?) {
                log(responseInfo!!, "cancel");
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                Log.e(
                    "${Tag} MainActivity",
                    Tag + " cancel onFailure ${errorInfo}--$invitation"
                )
            }

        })
    }

    private fun log(invitation: Invitation, funString: String) {
        Log.e(
            "MainActivity", Tag +
                    " $funString : ${invitation}"
        )
    }

    fun setTestListener() {
        manager?.setEventListener(object : GmsInvitationEventListener {
            override fun onFailure(invitation: Invitation, reason: InvitationFailureReason) {
                log(invitation, "onFailure")
                Log.e(
                    "MainActivity", "$Tag reason:${reason.reason}--$invitation"
                )
            }

            override fun onArrived(invitation: Invitation) {
                log(invitation, "onArrived")
                when (index) {
                    0 -> {
                        invitation.setResponseInfo("---extrainfo--testcancel")
                        testcancel(invitation)
                    }
                    1 -> {
                        invitation.setResponseInfo("---extrainfo--testAccept")
                        testAccept(invitation)
                    }
                    2 -> {
                        invitation.setResponseInfo("---extrainfo--testRefuse")
                        testRefuse(invitation)
                    }
                }
                index++
                if (index > 2) {
                    index = 0
                }
            }

            override fun onCanceled(invitation: Invitation) {
                log(invitation, "onCanceled ")
            }

            override fun onAccepted(invitation: Invitation) {
                invitation?.setResponseInfo("---extrainfo--response")
                log(invitation, "onAccepted ")
            }

            override fun onRefused(invitation: Invitation) {
                log(invitation, "onRefused ")
            }

        })
    }


}