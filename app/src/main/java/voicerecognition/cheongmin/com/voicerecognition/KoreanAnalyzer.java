package voicerecognition.cheongmin.com.voicerecognition;

import android.util.Log;

public class KoreanAnalyzer {
    public static final char[] CHO = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ',
            'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
    public static final char[] JUNG = {'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ',
            'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'};
    public static final char[] JONG = {' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ',
            'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ',
            'ㅍ', 'ㅎ'};
    private String text;

    public KoreanAnalyzer(String text) {
        this.text = text;
    }

    public String getText(){
        return text;
    }

    private char[][] getSyllable() {
        char[][] ret = new char[text.length()][3];
        for (int i = 0; i < text.length(); i++) {
            int code = text.charAt(i) - 0xAC00;
            int jong_idx = code % 28;
            int jung_idx = ((code - jong_idx) / 28) % 21;
            int cho_idx = (((code - jong_idx) / 28) - jung_idx) / 21;

            try {
                ret[i][0] = KoreanAnalyzer.CHO[cho_idx];
                ret[i][1] = KoreanAnalyzer.JUNG[jung_idx];
                ret[i][2] = KoreanAnalyzer.JONG[jong_idx];
            }
            catch(Exception e){
                Log.d("Exception", ""+text.charAt(i));
            }
        }
        return ret;
    }

    public double getSimilarity(KoreanAnalyzer target) {
        int total_factor = 0, correct_factor = 0;

        char[][] syllable1 = this.getSyllable();
        char[][] syllable2 = target.getSyllable();

        int idx = 0;
        while(idx < syllable1.length && idx < syllable2.length){
            total_factor += 3;
            for(int i = 0; i < 3; i++) if(syllable1[idx][i] == syllable2[idx][i]) correct_factor += 1;
            idx++;
        }
        for(; idx < syllable1.length; idx++) total_factor += 3;
        for(; idx < syllable2.length; idx++) total_factor += 3;

        return correct_factor / (double) total_factor;
    }
}
