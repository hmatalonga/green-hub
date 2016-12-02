/*
 * Copyright (c) 2016 Hugo Matalonga & João Paulo Fernandes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hmatalonga.greenhub.network;

import android.util.Log;

import hmatalonga.greenhub.Config;
import hmatalonga.greenhub.models.Specifications;
import hmatalonga.greenhub.models.data.Device;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Registers devices on server for first-run, connects device to server and provides uuid.
 *
 * Created by hugo on 25-03-2016.
 */
public class RegisterHandler {
    private static final String TAG = "RegisterHandler";

    private GreenHubAPIService mService;
    // 5s default for socket timeout
    private int mTimeout = 5000;

    public RegisterHandler(int timeout) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOCAL_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(GreenHubAPIService.class);
        mTimeout = timeout;
    }

    public Device registerClient() {
        Device device = new Device();
        device.uuId = Specifications.getAndroidId(null);
        device.timestamp = System.currentTimeMillis() / 1000.0;
        device.model = Specifications.getModel();
        device.manufacturer = Specifications.getManufacturer();
        device.brand = Specifications.getBrand();
        device.product = Specifications.getProductName();
        device.osVersion = Specifications.getOsVersion();
        device.kernelVersion = Specifications.getKernelVersion();
        device.serialNumber = Specifications.getBuildSerial();

        postRegistration(device);

        return device;
    }

    private void postRegistration(final Device device) {
        Call<Device> call = mService.createDevice(device);
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {

            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {

            }
        });
    }
}
