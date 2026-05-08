public class Styles {

    public static final String BASE = """
        * {
            -fx-font-family: 'Segoe UI', sans-serif;
        }
        .root-dark {
            -fx-background-color: #0d0f1a;
        }
        .title-label {
            -fx-font-size: 42px;
            -fx-font-weight: bold;
            -fx-text-fill: #ffffff;
            -fx-effect: dropshadow(gaussian, #4fc3f7, 18, 0.4, 0, 0);
        }
        .subtitle-label {
            -fx-font-size: 15px;
            -fx-text-fill: #78909c;
        }
        .section-label {
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-text-fill: #4fc3f7;
            -fx-padding: 0 0 4 0;
        }
        .card {
            -fx-background-color: #151929;
            -fx-background-radius: 12;
            -fx-border-color: #1e2a3a;
            -fx-border-radius: 12;
            -fx-border-width: 1;
            -fx-padding: 16;
        }
        .card-highlight {
            -fx-background-color: #0d1a2e;
            -fx-background-radius: 12;
            -fx-border-color: #4fc3f7;
            -fx-border-radius: 12;
            -fx-border-width: 1.5;
            -fx-padding: 16;
        }
        .btn-primary {
            -fx-background-color: #4fc3f7;
            -fx-text-fill: #0d0f1a;
            -fx-font-weight: bold;
            -fx-font-size: 14px;
            -fx-background-radius: 8;
            -fx-cursor: hand;
            -fx-padding: 10 24 10 24;
        }
        .btn-primary:hover {
            -fx-background-color: #81d4fa;
        }
        .btn-secondary {
            -fx-background-color: #1e2a3a;
            -fx-text-fill: #cfd8dc;
            -fx-font-size: 13px;
            -fx-background-radius: 8;
            -fx-cursor: hand;
            -fx-padding: 8 18 8 18;
            -fx-border-color: #2e3e52;
            -fx-border-radius: 8;
            -fx-border-width: 1;
        }
        .btn-secondary:hover {
            -fx-background-color: #263545;
            -fx-border-color: #4fc3f7;
        }
        .btn-danger {
            -fx-background-color: #c62828;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-size: 13px;
            -fx-background-radius: 8;
            -fx-cursor: hand;
            -fx-padding: 8 18 8 18;
        }
        .btn-danger:hover {
            -fx-background-color: #e53935;
        }
        .btn-success {
            -fx-background-color: #2e7d32;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-size: 14px;
            -fx-background-radius: 8;
            -fx-cursor: hand;
            -fx-padding: 10 24 10 24;
        }
        .btn-success:hover {
            -fx-background-color: #43a047;
        }
        .text-field-dark {
            -fx-background-color: #1e2a3a;
            -fx-text-fill: #eceff1;
            -fx-prompt-text-fill: #546e7a;
            -fx-border-color: #2e3e52;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-padding: 8 12 8 12;
            -fx-font-size: 14px;
        }
        .text-field-dark:focused {
            -fx-border-color: #4fc3f7;
        }
        .combo-dark {
            -fx-background-color: #1e2a3a;
            -fx-border-color: #2e3e52;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-text-fill: #eceff1;
            -fx-font-size: 13px;
        }
        .table-dark {
            -fx-background-color: #151929;
            -fx-border-color: #1e2a3a;
            -fx-text-fill: #eceff1;
        }
        .table-dark .column-header {
            -fx-background-color: #1a2436;
            -fx-text-fill: #4fc3f7;
            -fx-font-weight: bold;
            -fx-font-size: 12px;
        }
        .table-dark .table-row-cell {
            -fx-background-color: #151929;
            -fx-border-color: #1e2a3a;
            -fx-text-fill: #cfd8dc;
        }
        .table-dark .table-row-cell:selected {
            -fx-background-color: #1a3a5c;
        }
        .table-dark .table-row-cell:odd {
            -fx-background-color: #111524;
        }
        .stat-value {
            -fx-font-size: 28px;
            -fx-font-weight: bold;
            -fx-text-fill: #4fc3f7;
        }
        .stat-label {
            -fx-font-size: 11px;
            -fx-text-fill: #546e7a;
        }
        .nav-btn {
            -fx-background-color: transparent;
            -fx-text-fill: #78909c;
            -fx-font-size: 13px;
            -fx-cursor: hand;
            -fx-padding: 10 20 10 20;
            -fx-background-radius: 8;
        }
        .nav-btn:hover {
            -fx-background-color: #1e2a3a;
            -fx-text-fill: #eceff1;
        }
        .nav-btn-active {
            -fx-background-color: #1a3a5c;
            -fx-text-fill: #4fc3f7;
            -fx-font-size: 13px;
            -fx-cursor: hand;
            -fx-padding: 10 20 10 20;
            -fx-background-radius: 8;
            -fx-font-weight: bold;
        }
        .sport-card {
            -fx-background-color: #151929;
            -fx-background-radius: 16;
            -fx-border-color: #1e2a3a;
            -fx-border-radius: 16;
            -fx-border-width: 2;
            -fx-padding: 32;
            -fx-cursor: hand;
        }
        .sport-card:hover {
            -fx-background-color: #0d1a2e;
            -fx-border-color: #4fc3f7;
            -fx-effect: dropshadow(gaussian, #4fc3f740, 20, 0.3, 0, 0);
        }
        .injury-badge {
            -fx-background-color: #b71c1c;
            -fx-text-fill: white;
            -fx-font-size: 10px;
            -fx-background-radius: 4;
            -fx-padding: 2 6 2 6;
        }
        .separator-dark {
            -fx-background-color: #1e2a3a;
        }
        .scroll-pane-dark {
            -fx-background-color: transparent;
            -fx-border-color: transparent;
        }
        .scroll-pane-dark .viewport {
            -fx-background-color: transparent;
        }
        .scroll-pane-dark .scroll-bar {
            -fx-background-color: #151929;
        }
        .scroll-pane-dark .thumb {
            -fx-background-color: #2e3e52;
            -fx-background-radius: 4;
        }
        .week-label {
            -fx-font-size: 13px;
            -fx-text-fill: #4fc3f7;
            -fx-font-weight: bold;
        }
        .match-row {
            -fx-background-color: #151929;
            -fx-background-radius: 8;
            -fx-border-color: #1e2a3a;
            -fx-border-radius: 8;
            -fx-border-width: 1;
            -fx-padding: 10 16 10 16;
        }
        .match-row-played {
            -fx-background-color: #111524;
            -fx-background-radius: 8;
            -fx-border-color: #1a2e1a;
            -fx-border-radius: 8;
            -fx-border-width: 1;
            -fx-padding: 10 16 10 16;
        }
    """;
}
