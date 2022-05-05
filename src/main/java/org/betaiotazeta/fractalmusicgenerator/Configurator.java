package org.betaiotazeta.fractalmusicgenerator;

import java.util.List;

public class Configurator {

    public int getExtendedState() {
        return extendedState;
    }

    public void setExtendedState(int extendedState) {
        this.extendedState = extendedState;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public int getControlsDividerLocation() {
        return controlsDividerLocation;
    }

    public void setControlsDividerLocation(int controlsDividerLocation) {
        this.controlsDividerLocation = controlsDividerLocation;
    }

    public int getFractalDividerLocation() {
        return fractalDividerLocation;
    }

    public void setFractalDividerLocation(int fractalDividerLocation) {
        this.fractalDividerLocation = fractalDividerLocation;
    }
    
    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public double getMinA() {
        return minA;
    }

    public void setMinA(double minA) {
        this.minA = minA;
    }

    public double getMaxB() {
        return maxB;
    }

    public void setMaxB(double maxB) {
        this.maxB = maxB;
    }
    
    public int getFractalIndex() {
        return fractalIndex;
    }

    public void setFractalIndex(int fractalIndex) {
        this.fractalIndex = fractalIndex;
    }

    public int getMaxAudioIterationsSlider() {
        return maxAudioIterationsSlider;
    }

    public void setMaxAudioIterationsSlider(int maxAudioIterationsSlider) {
        this.maxAudioIterationsSlider = maxAudioIterationsSlider;
    }

    public int getMaxSdlSlider() {
        return maxSdlSlider;
    }

    public void setMaxSdlSlider(int maxSdlSlider) {
        this.maxSdlSlider = maxSdlSlider;
    }

    public boolean isSineRadioButton() {
        return sineRadioButton;
    }

    public void setSineRadioButton(boolean sineRadioButton) {
        this.sineRadioButton = sineRadioButton;
    }

    public boolean isDirectRadioButton() {
        return directRadioButton;
    }

    public void setDirectRadioButton(boolean directRadioButton) {
        this.directRadioButton = directRadioButton;
    }

    public boolean isWaveToggleButton() {
        return waveToggleButton;
    }

    public void setWaveToggleButton(boolean waveToggleButton) {
        this.waveToggleButton = waveToggleButton;
    }

    public boolean isMelodyToggleButton() {
        return melodyToggleButton;
    }

    public void setMelodyToggleButton(boolean melodyToggleButton) {
        this.melodyToggleButton = melodyToggleButton;
    }

    public boolean isDrumsToggleButton() {
        return drumsToggleButton;
    }

    public void setDrumsToggleButton(boolean drumsToggleButton) {
        this.drumsToggleButton = drumsToggleButton;
    }

    public boolean isDrawOrbitsCheckBox() {
        return drawOrbitsCheckBox;
    }

    public void setDrawOrbitsCheckBox(boolean drawOrbitsCheckBox) {
        this.drawOrbitsCheckBox = drawOrbitsCheckBox;
    }

    public int getOutlineSpeedSlider() {
        return outlineSpeedSlider;
    }

    public void setOutlineSpeedSlider(int outlineSpeedSlider) {
        this.outlineSpeedSlider = outlineSpeedSlider;
    }

    public int getDurationWaveSlider() {
        return durationWaveSlider;
    }

    public void setDurationWaveSlider(int durationWaveSlider) {
        this.durationWaveSlider = durationWaveSlider;
    }

    public int getInterpolationPointsSpinner() {
        return interpolationPointsSpinner;
    }

    public void setInterpolationPointsSpinner(int interpolationPointsSpinner) {
        this.interpolationPointsSpinner = interpolationPointsSpinner;
    }

    public int getKeepOrbitSpinner() {
        return keepOrbitSpinner;
    }

    public void setKeepOrbitSpinner(int keepOrbitSpinner) {
        this.keepOrbitSpinner = keepOrbitSpinner;
    }

    public int getBufferSizeSpinner() {
        return bufferSizeSpinner;
    }

    public void setBufferSizeSpinner(int bufferSizeSpinner) {
        this.bufferSizeSpinner = bufferSizeSpinner;
    }

    public int getBufferPollingSpinner() {
        return bufferPollingSpinner;
    }

    public void setBufferPollingSpinner(int bufferPollingSpinner) {
        this.bufferPollingSpinner = bufferPollingSpinner;
    }

    public int getVolumeWaveSlider() {
        return volumeWaveSlider;
    }

    public void setVolumeWaveSlider(int volumeWaveSlider) {
        this.volumeWaveSlider = volumeWaveSlider;
    }

    public double getMinZxDirectSpinner() {
        return minZxDirectSpinner;
    }

    public void setMinZxDirectSpinner(double minZxDirectSpinner) {
        this.minZxDirectSpinner = minZxDirectSpinner;
    }

    public double getMaxZxDirectSpinner() {
        return maxZxDirectSpinner;
    }

    public void setMaxZxDirectSpinner(double maxZxDirectSpinner) {
        this.maxZxDirectSpinner = maxZxDirectSpinner;
    }

    public double getMinZyDirectSpinner() {
        return minZyDirectSpinner;
    }

    public void setMinZyDirectSpinner(double minZyDirectSpinner) {
        this.minZyDirectSpinner = minZyDirectSpinner;
    }

    public double getMaxZyDirectSpinner() {
        return maxZyDirectSpinner;
    }

    public void setMaxZyDirectSpinner(double maxZyDirectSpinner) {
        this.maxZyDirectSpinner = maxZyDirectSpinner;
    }

    public double getMinZySineSpinner() {
        return minZySineSpinner;
    }

    public void setMinZySineSpinner(double minZySineSpinner) {
        this.minZySineSpinner = minZySineSpinner;
    }

    public double getMaxZySineSpinner() {
        return maxZySineSpinner;
    }

    public void setMaxZySineSpinner(double maxZySineSpinner) {
        this.maxZySineSpinner = maxZySineSpinner;
    }

    public int getMinFreqSpinner() {
        return minFreqSpinner;
    }

    public void setMinFreqSpinner(int minFreqSpinner) {
        this.minFreqSpinner = minFreqSpinner;
    }

    public int getMaxFreqSpinner() {
        return maxFreqSpinner;
    }

    public void setMaxFreqSpinner(int maxFreqSpinner) {
        this.maxFreqSpinner = maxFreqSpinner;
    }

    public int getMaxSequencersSlider() {
        return maxSequencersSlider;
    }

    public void setMaxSequencersSlider(int maxSequencersSlider) {
        this.maxSequencersSlider = maxSequencersSlider;
    }

    public int getNoteVelocitySlider() {
        return noteVelocitySlider;
    }

    public void setNoteVelocitySlider(int noteVelocitySlider) {
        this.noteVelocitySlider = noteVelocitySlider;
    }

    public int getSequenceSpeedSlider() {
        return sequenceSpeedSlider;
    }

    public void setSequenceSpeedSlider(int sequenceSpeedSlider) {
        this.sequenceSpeedSlider = sequenceSpeedSlider;
    }

    public int getNoteDurationSlider() {
        return noteDurationSlider;
    }

    public void setNoteDurationSlider(int noteDurationSlider) {
        this.noteDurationSlider = noteDurationSlider;
    }

    public boolean isPreventInterruptionsCheckBox() {
        return preventInterruptionsCheckBox;
    }

    public void setPreventInterruptionsCheckBox(boolean preventInterruptionsCheckBox) {
        this.preventInterruptionsCheckBox = preventInterruptionsCheckBox;
    }

    public boolean isSkipDivergentMidiCheckBox() {
        return skipDivergentMidiCheckBox;
    }

    public void setSkipDivergentMidiCheckBox(boolean skipDivergentMidiCheckBox) {
        this.skipDivergentMidiCheckBox = skipDivergentMidiCheckBox;
    }

    public boolean isInstrChangeEffectCheckBox() {
        return instrChangeEffectCheckBox;
    }

    public void setInstrChangeEffectCheckBox(boolean instrChangeEffectCheckBox) {
        this.instrChangeEffectCheckBox = instrChangeEffectCheckBox;
    }

    public boolean isPanMidiEffectCheckBox() {
        return panMidiEffectCheckBox;
    }

    public void setPanMidiEffectCheckBox(boolean panMidiEffectCheckBox) {
        this.panMidiEffectCheckBox = panMidiEffectCheckBox;
    }

    public double getMinZxMidiSpinner() {
        return minZxMidiSpinner;
    }

    public void setMinZxMidiSpinner(double minZxMidiSpinner) {
        this.minZxMidiSpinner = minZxMidiSpinner;
    }

    public double getMaxZxMidiSpinner() {
        return maxZxMidiSpinner;
    }

    public void setMaxZxMidiSpinner(double maxZxMidiSpinner) {
        this.maxZxMidiSpinner = maxZxMidiSpinner;
    }

    public int getMinMidiEffectSpinner() {
        return minMidiEffectSpinner;
    }

    public void setMinMidiEffectSpinner(int minMidiEffectSpinner) {
        this.minMidiEffectSpinner = minMidiEffectSpinner;
    }

    public int getMaxMidiEffectSpinner() {
        return maxMidiEffectSpinner;
    }

    public void setMaxMidiEffectSpinner(int maxMidiEffectSpinner) {
        this.maxMidiEffectSpinner = maxMidiEffectSpinner;
    }

    public double getMinZyMidiSpinner() {
        return minZyMidiSpinner;
    }

    public void setMinZyMidiSpinner(double minZyMidiSpinner) {
        this.minZyMidiSpinner = minZyMidiSpinner;
    }

    public double getMaxZyMidiSpinner() {
        return maxZyMidiSpinner;
    }

    public void setMaxZyMidiSpinner(double maxZyMidiSpinner) {
        this.maxZyMidiSpinner = maxZyMidiSpinner;
    }

    public int getMinNoteSpinner() {
        return minNoteSpinner;
    }

    public void setMinNoteSpinner(int minNoteSpinner) {
        this.minNoteSpinner = minNoteSpinner;
    }

    public int getMaxNoteSpinner() {
        return maxNoteSpinner;
    }

    public void setMaxNoteSpinner(int maxNoteSpinner) {
        this.maxNoteSpinner = maxNoteSpinner;
    }

    public boolean isVerboseCheckBox() {
        return verboseCheckBox;
    }

    public void setVerboseCheckBox(boolean verboseCheckBox) {
        this.verboseCheckBox = verboseCheckBox;
    }

    public boolean isSkipDivergentWaveCheckBox() {
        return skipDivergentWaveCheckBox;
    }

    public void setSkipDivergentWaveCheckBox(boolean skipDivergentWaveCheckBox) {
        this.skipDivergentWaveCheckBox = skipDivergentWaveCheckBox;
    }

    public boolean isDrumsChangeEffectCheckBox() {
        return drumsChangeEffectCheckBox;
    }

    public void setDrumsChangeEffectCheckBox(boolean drumsChangeEffectCheckBox) {
        this.drumsChangeEffectCheckBox = drumsChangeEffectCheckBox;
    }

    public int getDrumsNoteDurationSlider() {
        return drumsNoteDurationSlider;
    }

    public void setDrumsNoteDurationSlider(int drumsNoteDurationSlider) {
        this.drumsNoteDurationSlider = drumsNoteDurationSlider;
    }

    public int getDrumsMaxMidiEffectSpinner() {
        return drumsMaxMidiEffectSpinner;
    }

    public void setDrumsMaxMidiEffectSpinner(int drumsMaxMidiEffectSpinner) {
        this.drumsMaxMidiEffectSpinner = drumsMaxMidiEffectSpinner;
    }

    public int getDrumsMaxNoteSpinner() {
        return drumsMaxNoteSpinner;
    }

    public void setDrumsMaxNoteSpinner(int drumsMaxNoteSpinner) {
        this.drumsMaxNoteSpinner = drumsMaxNoteSpinner;
    }

    public int getDrumsMaxSequencersSlider() {
        return drumsMaxSequencersSlider;
    }

    public void setDrumsMaxSequencersSlider(int drumsMaxSequencersSlider) {
        this.drumsMaxSequencersSlider = drumsMaxSequencersSlider;
    }

    public double getDrumsMaxZxMidiSpinner() {
        return drumsMaxZxMidiSpinner;
    }

    public void setDrumsMaxZxMidiSpinner(double drumsMaxZxMidiSpinner) {
        this.drumsMaxZxMidiSpinner = drumsMaxZxMidiSpinner;
    }

    public double getDrumsMaxZyMidiSpinner() {
        return drumsMaxZyMidiSpinner;
    }

    public void setDrumsMaxZyMidiSpinner(double drumsMaxZyMidiSpinner) {
        this.drumsMaxZyMidiSpinner = drumsMaxZyMidiSpinner;
    }

    public int getDrumsMinMidiEffectSpinner() {
        return drumsMinMidiEffectSpinner;
    }

    public void setDrumsMinMidiEffectSpinner(int drumsMinMidiEffectSpinner) {
        this.drumsMinMidiEffectSpinner = drumsMinMidiEffectSpinner;
    }

    public int getDrumsMinNoteSpinner() {
        return drumsMinNoteSpinner;
    }

    public void setDrumsMinNoteSpinner(int drumsMinNoteSpinner) {
        this.drumsMinNoteSpinner = drumsMinNoteSpinner;
    }

    public double getDrumsMinZxMidiSpinner() {
        return drumsMinZxMidiSpinner;
    }

    public void setDrumsMinZxMidiSpinner(double drumsMinZxMidiSpinner) {
        this.drumsMinZxMidiSpinner = drumsMinZxMidiSpinner;
    }

    public double getDrumsMinZyMidiSpinner() {
        return drumsMinZyMidiSpinner;
    }

    public void setDrumsMinZyMidiSpinner(double drumsMinZyMidiSpinner) {
        this.drumsMinZyMidiSpinner = drumsMinZyMidiSpinner;
    }

    public boolean isDrumsPanMidiEffectCheckBox() {
        return drumsPanMidiEffectCheckBox;
    }

    public void setDrumsPanMidiEffectCheckBox(boolean drumsPanMidiEffectCheckBox) {
        this.drumsPanMidiEffectCheckBox = drumsPanMidiEffectCheckBox;
    }

    public boolean isDrumsPreventInterruptionsCheckBox() {
        return drumsPreventInterruptionsCheckBox;
    }

    public void setDrumsPreventInterruptionsCheckBox(boolean drumsPreventInterruptionsCheckBox) {
        this.drumsPreventInterruptionsCheckBox = drumsPreventInterruptionsCheckBox;
    }

    public int getDrumsSequenceSpeedSlider() {
        return drumsSequenceSpeedSlider;
    }

    public void setDrumsSequenceSpeedSlider(int drumsSequenceSpeedSlider) {
        this.drumsSequenceSpeedSlider = drumsSequenceSpeedSlider;
    }

    public boolean isDrumsSkipDivergentMidiCheckBox() {
        return drumsSkipDivergentMidiCheckBox;
    }

    public void setDrumsSkipDivergentMidiCheckBox(boolean drumsSkipDivergentMidiCheckBox) {
        this.drumsSkipDivergentMidiCheckBox = drumsSkipDivergentMidiCheckBox;
    }

    public int getDrumsVelocitySlider() {
        return drumsVelocitySlider;
    }

    public void setDrumsVelocitySlider(int drumsVelocitySlider) {
        this.drumsVelocitySlider = drumsVelocitySlider;
    }

    public int getOutlineSpeedRandomnessSlider() {
        return outlineSpeedRandomnessSlider;
    }

    public void setOutlineSpeedRandomnessSlider(int outlineSpeedRandomnessSlider) {
        this.outlineSpeedRandomnessSlider = outlineSpeedRandomnessSlider;
    }

    public boolean isDrawCoordinatesCheckBox() {
        return drawCoordinatesCheckBox;
    }

    public void setDrawCoordinatesCheckBox(boolean drawCoordinatesCheckBox) {
        this.drawCoordinatesCheckBox = drawCoordinatesCheckBox;
    }

    public List<String> getInstrumentsMelodyList() {
        return instrumentsMelodyList;
    }

    public void setInstrumentsMelodyList(List<String> instrumentsMelodyList) {
        this.instrumentsMelodyList = instrumentsMelodyList;
    }

    public List<String> getDrumkitsList() {
        return drumkitsList;
    }

    public void setDrumkitsList(List<String> drumkitsList) {
        this.drumkitsList = drumkitsList;
    }

    public String getExternalDeviceName() {
        return externalDeviceName;
    }

    public void setExternalDeviceName(String externalDeviceName) {
        this.externalDeviceName = externalDeviceName;
    }

    public boolean isDumpToggleButton() {
        return dumpToggleButton;
    }

    public void setDumpToggleButton(boolean dumpToggleButton) {
        this.dumpToggleButton = dumpToggleButton;
    }

    public boolean isGervillToggleButton() {
        return gervillToggleButton;
    }

    public void setGervillToggleButton(boolean gervillToggleButton) {
        this.gervillToggleButton = gervillToggleButton;
    }

    public boolean isJackToggleButton() {
        return jackToggleButton;
    }

    public void setJackToggleButton(boolean jackToggleButton) {
        this.jackToggleButton = jackToggleButton;
    }

    public boolean isLockOutlineToggleButton() {
        return lockOutlineToggleButton;
    }

    public void setLockOutlineToggleButton(boolean lockOutlineToggleButton) {
        this.lockOutlineToggleButton = lockOutlineToggleButton;
    }

    public boolean isInitInstrumentsCheckBox() {
        return initInstrumentsCheckBox;
    }

    public void setInitInstrumentsCheckBox(boolean initInstrumentsCheckBox) {
        this.initInstrumentsCheckBox = initInstrumentsCheckBox;
    }

    public boolean isDrumsInitInstrumentsCheckBox() {
        return drumsInitInstrumentsCheckBox;
    }

    public void setDrumsInitInstrumentsCheckBox(boolean drumsInitInstrumentsCheckBox) {
        this.drumsInitInstrumentsCheckBox = drumsInitInstrumentsCheckBox;
    }

    public boolean isHistogramCheckBox() {
        return histogramCheckBox;
    }

    public void setHistogramCheckBox(boolean histogramCheckBox) {
        this.histogramCheckBox = histogramCheckBox;
    }

    public boolean isSmoothCheckBox() {
        return smoothCheckBox;
    }

    public void setSmoothCheckBox(boolean smoothCheckBox) {
        this.smoothCheckBox = smoothCheckBox;
    }

    public int getMaxImageIterationsSlider() {
        return maxImageIterationsSlider;
    }

    public void setMaxImageIterationsSlider(int maxImageIterationsSlider) {
        this.maxImageIterationsSlider = maxImageIterationsSlider;
    }

    public boolean isAutoImageIterationsCheckBox() {
        return autoImageIterationsCheckBox;
    }

    public void setAutoImageIterationsCheckBox(boolean autoImageIterationsCheckBox) {
        this.autoImageIterationsCheckBox = autoImageIterationsCheckBox;
    }

    public int getColor0Button() {
        return color0Button;
    }

    public void setColor0Button(int color0Button) {
        this.color0Button = color0Button;
    }

    public int getColor1Button() {
        return color1Button;
    }

    public void setColor1Button(int color1Button) {
        this.color1Button = color1Button;
    }

    public int getColor2Button() {
        return color2Button;
    }

    public void setColor2Button(int color2Button) {
        this.color2Button = color2Button;
    }

    public int getColor3Button() {
        return color3Button;
    }

    public void setColor3Button(int color3Button) {
        this.color3Button = color3Button;
    }

    public int getColor4Button() {
        return color4Button;
    }

    public void setColor4Button(int color4Button) {
        this.color4Button = color4Button;
    }

    public int getColor5Button() {
        return color5Button;
    }

    public void setColor5Button(int color5Button) {
        this.color5Button = color5Button;
    }

    public int getColor6Button() {
        return color6Button;
    }

    public void setColor6Button(int color6Button) {
        this.color6Button = color6Button;
    }

    public int getColor7Button() {
        return color7Button;
    }

    public void setColor7Button(int color7Button) {
        this.color7Button = color7Button;
    }

    public int getColor8Button() {
        return color8Button;
    }

    public void setColor8Button(int color8Button) {
        this.color8Button = color8Button;
    }

    public int getColor9Button() {
        return color9Button;
    }

    public void setColor9Button(int color9Button) {
        this.color9Button = color9Button;
    }

    public int getColor0Slider() {
        return color0Slider;
    }

    public void setColor0Slider(int color0Slider) {
        this.color0Slider = color0Slider;
    }

    public int getColor1Slider() {
        return color1Slider;
    }

    public void setColor1Slider(int color1Slider) {
        this.color1Slider = color1Slider;
    }

    public int getColor2Slider() {
        return color2Slider;
    }

    public void setColor2Slider(int color2Slider) {
        this.color2Slider = color2Slider;
    }

    public int getColor3Slider() {
        return color3Slider;
    }

    public void setColor3Slider(int color3Slider) {
        this.color3Slider = color3Slider;
    }

    public int getColor4Slider() {
        return color4Slider;
    }

    public void setColor4Slider(int color4Slider) {
        this.color4Slider = color4Slider;
    }

    public int getColor5Slider() {
        return color5Slider;
    }

    public void setColor5Slider(int color5Slider) {
        this.color5Slider = color5Slider;
    }

    public int getColor6Slider() {
        return color6Slider;
    }

    public void setColor6Slider(int color6Slider) {
        this.color6Slider = color6Slider;
    }

    public int getColor7Slider() {
        return color7Slider;
    }

    public void setColor7Slider(int color7Slider) {
        this.color7Slider = color7Slider;
    }

    public int getColor8Slider() {
        return color8Slider;
    }

    public void setColor8Slider(int color8Slider) {
        this.color8Slider = color8Slider;
    }

    public int getReverbSlider() {
        return reverbSlider;
    }

    public void setReverbSlider(int reverbSlider) {
        this.reverbSlider = reverbSlider;
    }
    
    public int getChorusSlider() {
        return chorusSlider;
    }

    public void setChorusSlider(int chorusSlider) {
        this.chorusSlider = chorusSlider;
    }

    public int getDrumsReverbSlider() {
        return drumsReverbSlider;
    }

    public void setDrumsReverbSlider(int drumsReverbSlider) {
        this.drumsReverbSlider = drumsReverbSlider;
    }

    public int getDrumsChorusSlider() {
        return drumsChorusSlider;
    }

    public void setDrumsChorusSlider(int drumsChorusSlider) {
        this.drumsChorusSlider = drumsChorusSlider;
    }

    public int getOutlineSkipSlider() {
        return outlineSkipSlider;
    }

    public void setOutlineSkipSlider(int outlineSkipSlider) {
        this.outlineSkipSlider = outlineSkipSlider;
    }

    public boolean isLoopingCheckBox() {
        return loopingCheckBox;
    }

    public void setLoopingCheckBox(boolean loopingCheckBox) {
        this.loopingCheckBox = loopingCheckBox;
    }

    public double getPaletteDensitySpinner() {
        return paletteDensitySpinner;
    }

    public void setPaletteDensitySpinner(double paletteDensitySpinner) {
        this.paletteDensitySpinner = paletteDensitySpinner;
    }    

    // Variables
    private int extendedState;
    private int frameWidth;
    private int frameHeight;
    private int controlsDividerLocation;
    private int fractalDividerLocation;
    private double zoom;
    private double minA;
    private double maxB;
    private int fractalIndex;
    private int maxAudioIterationsSlider;
    private int maxSdlSlider;
    private boolean sineRadioButton;
    private boolean directRadioButton;
    private boolean waveToggleButton;
    private boolean melodyToggleButton;
    private boolean drumsToggleButton;
    private boolean drawOrbitsCheckBox;
    private int outlineSpeedSlider;
    private int durationWaveSlider;
    private int interpolationPointsSpinner;
    private int keepOrbitSpinner;
    private int bufferSizeSpinner;
    private int bufferPollingSpinner;
    private int volumeWaveSlider;
    private double minZxDirectSpinner;
    private double maxZxDirectSpinner;
    private double minZyDirectSpinner;
    private double maxZyDirectSpinner;
    private double minZySineSpinner;
    private double maxZySineSpinner;
    private int minFreqSpinner;
    private int maxFreqSpinner;
    private int maxSequencersSlider;
    private int noteVelocitySlider;
    private int sequenceSpeedSlider;
    private int noteDurationSlider;
    private boolean preventInterruptionsCheckBox;
    private boolean skipDivergentMidiCheckBox;
    private boolean instrChangeEffectCheckBox;
    private boolean panMidiEffectCheckBox;
    private double minZxMidiSpinner;
    private double maxZxMidiSpinner;
    private int minMidiEffectSpinner;
    private int maxMidiEffectSpinner;
    private double minZyMidiSpinner;
    private double maxZyMidiSpinner;
    private int minNoteSpinner;
    private int maxNoteSpinner;
    private boolean verboseCheckBox;
    private boolean skipDivergentWaveCheckBox;
    private boolean drumsChangeEffectCheckBox;
    private int drumsNoteDurationSlider;
    private int drumsMaxMidiEffectSpinner;
    private int drumsMaxNoteSpinner;
    private int drumsMaxSequencersSlider;
    private double drumsMaxZxMidiSpinner;
    private double drumsMaxZyMidiSpinner;
    private int drumsMinMidiEffectSpinner;
    private int drumsMinNoteSpinner;
    private double drumsMinZxMidiSpinner;
    private double drumsMinZyMidiSpinner;
    private boolean drumsPanMidiEffectCheckBox;
    private boolean drumsPreventInterruptionsCheckBox;
    private int drumsSequenceSpeedSlider;
    private boolean drumsSkipDivergentMidiCheckBox;
    private int drumsVelocitySlider;
    private int outlineSpeedRandomnessSlider;
    private boolean drawCoordinatesCheckBox;
    private List<String> instrumentsMelodyList;
    private List<String> drumkitsList;
    private String externalDeviceName;
    private boolean dumpToggleButton;
    private boolean gervillToggleButton;
    private boolean jackToggleButton;
    private boolean lockOutlineToggleButton;
    private boolean initInstrumentsCheckBox;
    private boolean drumsInitInstrumentsCheckBox;
    private boolean histogramCheckBox;
    private boolean smoothCheckBox;
    private int maxImageIterationsSlider;
    private boolean autoImageIterationsCheckBox;
    private int color0Button;
    private int color1Button;
    private int color2Button;
    private int color3Button;
    private int color4Button;
    private int color5Button;
    private int color6Button;
    private int color7Button;
    private int color8Button;
    private int color9Button;
    private int color0Slider;
    private int color1Slider;
    private int color2Slider;
    private int color3Slider;
    private int color4Slider;
    private int color5Slider;
    private int color6Slider;
    private int color7Slider;
    private int color8Slider;
    private int reverbSlider;
    private int chorusSlider;
    private int drumsReverbSlider;
    private int drumsChorusSlider;
    private int outlineSkipSlider;
    private boolean loopingCheckBox;
    private double paletteDensitySpinner;
}
