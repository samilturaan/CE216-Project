public class Styles {

    public static final String BG_DEEP    = "#080c18";
    public static final String BG_PANEL   = "#0d1222";
    public static final String BG_CARD    = "#111827";
    public static final String BORDER     = "#1e2d45";
    public static final String ACCENT     = "#3b82f6";
    public static final String ACCENT2    = "#60a5fa";
    public static final String TEXT_PRI   = "#f0f4ff";
    public static final String TEXT_SEC   = "#64748b";
    public static final String SUCCESS    = "#22c55e";
    public static final String DANGER     = "#ef4444";

    public static final String BASE = """
        * { -fx-font-family: 'Segoe UI', 'Tahoma', sans-serif; }
        .root-bg { -fx-background-color: #080c18; }
        .panel-bg { -fx-background-color: #0d1222; }
        .card {
            -fx-background-color: #111827;
            -fx-background-radius: 14;
            -fx-border-color: #1e2d45;
            -fx-border-radius: 14;
            -fx-border-width: 1;
        }
        .card-accent {
            -fx-background-color: #111827;
            -fx-background-radius: 14;
            -fx-border-color: #3b82f6;
            -fx-border-radius: 14;
            -fx-border-width: 1.5;
        }
        .page-title { -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff; }
        .card-title { -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #64748b; }
        .big-number { -fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #3b82f6; }
        .btn-primary {
            -fx-background-color: #3b82f6; -fx-text-fill: white;
            -fx-font-weight: bold; -fx-font-size: 13px;
            -fx-background-radius: 8; -fx-cursor: hand;
            -fx-padding: 9 22 9 22; -fx-border-width: 0;
        }
        .btn-primary:hover { -fx-background-color: #60a5fa; }
        .btn-ghost {
            -fx-background-color: transparent; -fx-text-fill: #64748b;
            -fx-font-size: 13px; -fx-background-radius: 8; -fx-cursor: hand;
            -fx-padding: 9 16 9 16; -fx-border-color: #1e2d45;
            -fx-border-radius: 8; -fx-border-width: 1;
        }
        .btn-ghost:hover { -fx-border-color: #3b82f6; -fx-text-fill: #f0f4ff; }
        .nav-item {
            -fx-background-color: transparent; -fx-text-fill: #475569;
            -fx-font-size: 13px; -fx-cursor: hand;
            -fx-padding: 11 16 11 16; -fx-background-radius: 10;
            -fx-alignment: center-left; -fx-border-width: 0;
        }
        .nav-item:hover { -fx-background-color: #111827; -fx-text-fill: #94a3b8; }
        .nav-item-active {
            -fx-background-color: #1e3a5f; -fx-text-fill: #60a5fa;
            -fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand;
            -fx-padding: 11 16 11 16; -fx-background-radius: 10;
            -fx-alignment: center-left; -fx-border-width: 0;
        }
        .badge-win { -fx-background-color: #14532d; -fx-text-fill: #22c55e; -fx-font-size: 10px; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 2 7 2 7; }
        .badge-loss { -fx-background-color: #7f1d1d; -fx-text-fill: #ef4444; -fx-font-size: 10px; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 2 7 2 7; }
        .badge-draw { -fx-background-color: #292524; -fx-text-fill: #a8a29e; -fx-font-size: 10px; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 2 7 2 7; }
        .badge-upcoming { -fx-background-color: #1e3a5f; -fx-text-fill: #60a5fa; -fx-font-size: 10px; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 2 7 2 7; }
        .badge-injured { -fx-background-color: #7f1d1d; -fx-text-fill: #ef4444; -fx-font-size: 10px; -fx-background-radius: 4; -fx-padding: 2 6 2 6; }
        .badge-fit { -fx-background-color: #14532d; -fx-text-fill: #22c55e; -fx-font-size: 10px; -fx-background-radius: 4; -fx-padding: 2 6 2 6; }
        .tfield {
            -fx-background-color: #111827; -fx-text-fill: #f0f4ff;
            -fx-prompt-text-fill: #334155; -fx-border-color: #1e2d45;
            -fx-border-radius: 8; -fx-background-radius: 8;
            -fx-padding: 10 14 10 14; -fx-font-size: 14px;
        }
        .tfield:focused { -fx-border-color: #3b82f6; }
        .scroll-clean { -fx-background-color: transparent; -fx-border-color: transparent; }
        .scroll-clean .viewport { -fx-background-color: transparent; }
        .scroll-clean .scroll-bar { -fx-background-color: #0d1222; }
        .scroll-clean .thumb { -fx-background-color: #1e2d45; -fx-background-radius: 4; }
    """;
}