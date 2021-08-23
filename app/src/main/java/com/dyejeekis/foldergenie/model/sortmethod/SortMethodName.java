package com.dyejeekis.foldergenie.model.sortmethod;

import androidx.annotation.NonNull;

import com.dyejeekis.foldergenie.model.AlphanumRange;
import com.dyejeekis.foldergenie.util.GeneralUtil;

import java.io.File;
import java.util.List;

public class SortMethodName extends SortMethod {

    private final List<AlphanumRange> alphanumRanges;

    public SortMethodName(@NonNull List<AlphanumRange> alphanumRanges, boolean addToArchive,
                          boolean addToFilename) {
        super(addToArchive, addToFilename);
        if (alphanumRanges.isEmpty())
            throw new IllegalArgumentException("Sort method must have at least one alphanumeric range to be valid");
        if (rangesOverlap(alphanumRanges))
            throw new IllegalArgumentException("Overlap detected in selected alphanumeric ranges");
        this.alphanumRanges = alphanumRanges;
    }

    @Override
    public boolean addToFilename() {
        return false;
    }

    @Override
    public String getDirName(File file) {
        for (AlphanumRange range : alphanumRanges) {
            String start = range.getStartStr();
            String end = range.getEndStr();
            if (start == null || file.getName().compareTo(start) >= 0) {
                if (end == null || file.getName().compareTo(end) <= 0)
                    return range.toString();
            }
        }
        return "";
    }

    @Override
    public SortMethodType getType() {
        return SortMethodType.NAME;
    }

    @NonNull
    @Override
    public String toString() {
        String s = "Sort in folders based on name ";
        s = s.concat("'" + GeneralUtil.listToString(alphanumRanges, "', '") + "'");
        return s + super.toString();
    }

    public List<AlphanumRange> getAlphanumRanges() {
        return alphanumRanges;
    }

    private boolean rangesOverlap(List<AlphanumRange> alphanumRanges) {
        // TODO: 8/9/2021
        return false;
    }
}
