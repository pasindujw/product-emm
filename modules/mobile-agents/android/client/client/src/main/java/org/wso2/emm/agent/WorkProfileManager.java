/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.emm.agent;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import org.wso2.emm.agent.api.ApplicationManager;
import org.wso2.emm.agent.services.AgentDeviceAdminReceiver;

import static android.app.admin.DevicePolicyManager.ACTION_PROVISION_MANAGED_PROFILE;
import static android.app.admin.DevicePolicyManager.EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME;

public class WorkProfileManager extends Activity {

    private static final int REQUEST_PROVISION_MANAGED_PROFILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        provisionManagedProfile();

    }

    private void provisionManagedProfile() {
        Activity activity = this;
        if (null == activity) {
            return;
        }
        Intent intent = new Intent(ACTION_PROVISION_MANAGED_PROFILE);
        intent.putExtra(EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME, activity.getApplicationContext().getPackageName());
        ApplicationManager am = new ApplicationManager(this.getApplicationContext());
        am.uninstallApplication("org.wso2.emm.agent");
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_PROVISION_MANAGED_PROFILE);
            activity.finish();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Two toasts to increase the duration
            Toast.makeText(this,
                    "Only Agent in Work Profile is needed after this step. You can uninstall Agent in Personal Profile.",
                    Toast.LENGTH_LONG).show();
            Toast.makeText(this,
                    "Only Agent in Work Profile is needed after this step. You can uninstall Agent in Personal Profile now.",
                    Toast.LENGTH_LONG).show();
            Toast.makeText(this,
                    "Only Agent in Work Profile is needed after this step. You can uninstall Agent in Personal Profile now.",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Device provisioning is not enabled. Stopping.", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PROVISION_MANAGED_PROFILE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Provisioning done.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ServerDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            } else {
                Toast.makeText(this, "Provisioning failed.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}