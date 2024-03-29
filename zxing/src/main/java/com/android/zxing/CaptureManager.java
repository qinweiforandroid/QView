/*
 * Copyright (C) 2019 Jenly Yu
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
package com.android.zxing;


import com.android.zxing.camera.CameraManager;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface CaptureManager {

    /**
     * Get {@link CameraManager}
     *
     * @return {@link CameraManager}
     */
    CameraManager getCameraManager();

    /**
     * Get {@link BeepManager}
     *
     * @return {@link BeepManager}
     */
    BeepManager getBeepManager();

    /**
     * Get {@link AmbientLightManager}
     *
     * @return {@link AmbientLightManager}
     */
    AmbientLightManager getAmbientLightManager();

    /**
     * Get {@link  InactivityTimer}
     *
     * @return {@link  InactivityTimer}
     */
    InactivityTimer getInactivityTimer();
}
