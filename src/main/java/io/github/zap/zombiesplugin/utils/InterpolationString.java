package io.github.zap.zombiesplugin.utils;

public abstract class InterpolationString {
    public String startSequence;
    public String endSequence;

    public String evalString(String expr) {

        StringBuilder builder = new StringBuilder();
        StringBuilder buffer = new StringBuilder();
        boolean isOpen = false;
        int startSeqIndex = 0;
        int endSeqIndex = 0;

        for (char i : expr.toCharArray()) {
            if (!isOpen && i == startSequence.charAt(startSeqIndex)) {
                buffer.append(i);
                startSeqIndex++;
                isOpen = startSeqIndex >= startSequence.length();
                continue;
            } else if (isOpen && i == endSequence.charAt(endSeqIndex)) {
                buffer.append(i);
                endSeqIndex++;

                if (endSeqIndex >= endSequence.length()) {
                    isOpen = false;
                    startSeqIndex = 0;
                    endSeqIndex = 0;
                    builder.append(evalExpr(buffer.toString()));
                    buffer.setLength(0);
                }
            } else {
                if (isOpen) {
                    buffer.append(i);
                } else {
                    builder.append(i);
                }
            }
        }

        // append any remaining content that don't have closing sequence
        builder.append(buffer);

        return builder.toString();
    }

    protected abstract String evalExpr(String expr);

    protected String removeExprNotation(String expr) {
        String trimStart = expr.substring(startSequence.length());
        return trimStart.substring(0, trimStart.length() - endSequence.length());
    }
}
