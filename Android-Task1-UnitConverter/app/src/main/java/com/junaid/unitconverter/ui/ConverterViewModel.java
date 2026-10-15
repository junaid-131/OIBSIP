package com.junaid.unitconverter.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.junaid.unitconverter.data.HistoryRepository;
import com.junaid.unitconverter.data.UnitRegistry;
import com.junaid.unitconverter.model.Category;
import com.junaid.unitconverter.model.ConversionRecord;
import com.junaid.unitconverter.model.Unit;
import com.junaid.unitconverter.util.NumberFormatter;
import com.junaid.unitconverter.util.UnitConverter;

import java.util.List;

public class ConverterViewModel extends AndroidViewModel {

    private final HistoryRepository historyRepo;

    private final MutableLiveData<String> input = new MutableLiveData<>("");
    private final MutableLiveData<List<Unit>> units = new MutableLiveData<>();
    private final MutableLiveData<Unit> fromUnit = new MutableLiveData<>();
    private final MutableLiveData<Unit> toUnit = new MutableLiveData<>();
    private final MutableLiveData<String> result = new MutableLiveData<>("");
    private final MutableLiveData<String> errorEvent = new MutableLiveData<>(null);

    public ConverterViewModel(@NonNull Application app) {
        super(app);
        historyRepo = new HistoryRepository(app);
        loadUnitsFor(Category.LENGTH);
    }

    public LiveData<String> getInput() { return input; }
    public LiveData<List<Unit>> getUnits() { return units; }
    public LiveData<Unit> getFromUnit() { return fromUnit; }
    public LiveData<Unit> getToUnit() { return toUnit; }
    public LiveData<String> getResult() { return result; }
    public LiveData<String> getErrorEvent() { return errorEvent; }
    public LiveData<List<ConversionRecord>> getHistory() { return historyRepo.getHistory(); }

    public void setCategory(Category c) {
        loadUnitsFor(c);
        convertNow();
    }

    private void loadUnitsFor(Category c) {
        List<Unit> list = UnitRegistry.getUnits(c);
        units.setValue(list);
        if (list.size() >= 2) {
            fromUnit.setValue(list.get(0));
            toUnit.setValue(list.get(1));
        } else if (list.size() == 1) {
            fromUnit.setValue(list.get(0));
            toUnit.setValue(list.get(0));
        }
    }

    public void appendKey(String key) {
        String cur = input.getValue();
        if (cur == null) cur = "";

        if ("⌫".equals(key)) {
            if (!cur.isEmpty()) cur = cur.substring(0, cur.length() - 1);
        } else if (".".equals(key)) {
            if (cur.contains(".")) return;
            if (cur.isEmpty()) cur = "0.";
            else cur = cur + ".";
        } else {
            if (cur.equals("0")) cur = key;
            else cur = cur + key;
            if (cur.length() > 12) cur = cur.substring(0, 12);
        }
        input.setValue(cur);
        convertNow();
    }

    public void clearInput() {
        input.setValue("");
        convertNow();
    }

    public void setFromUnit(Unit u) { fromUnit.setValue(u); convertNow(); }
    public void setToUnit(Unit u) { toUnit.setValue(u); convertNow(); }

    public void swapUnits() {
        Unit f = fromUnit.getValue();
        Unit t = toUnit.getValue();
        if (f == null || t == null) return;
        fromUnit.setValue(t);
        toUnit.setValue(f);
        convertNow();
    }

    private void convertNow() {
        String text = input.getValue();
        Unit from = fromUnit.getValue();
        Unit to = toUnit.getValue();

        if (text == null || text.trim().isEmpty() || from == null || to == null) {
            result.setValue("");
            return;
        }
        try {
            double value = Double.parseDouble(text);
            double converted = UnitConverter.convert(value, from, to);
            result.setValue(NumberFormatter.format(converted) + " " + to.getSymbol());
        } catch (NumberFormatException e) {
            result.setValue("");
        } catch (Exception e) {
            result.setValue("");
            errorEvent.setValue(e.getMessage());
        }
    }

    public void recordConversion() {
        String text = input.getValue();
        Unit from = fromUnit.getValue();
        Unit to = toUnit.getValue();
        String res = result.getValue();
        if (text == null || text.isEmpty() || res == null || res.isEmpty()) return;
        if (from == null || to == null) return;
        historyRepo.add(text + " " + from.getSymbol(), res);
    }

    public void clearHistory() { historyRepo.clear(); }
    public void clearErrorEvent() { errorEvent.setValue(null); }
}