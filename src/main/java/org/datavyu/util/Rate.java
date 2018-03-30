package org.datavyu.util;


/**
 * Supported Rates
 */
//TODO: Enter Seek Mode for backward Rates very fast rate
public enum Rate {
    MX32(-32F) {
        @Override
        public Rate next() {
            return MX16;
        }

        @Override
        public Rate previous() {
            return this;
        }
    },
    MX16(-16F) {
        @Override
        public Rate next() {
            return MX8;
        }

        @Override
        public Rate previous() {
            return MX32;
        }
    },
    MX8(-8F) {
        @Override
        public Rate next() {
            return MX4;
        }

        @Override
        public Rate previous() {
            return MX16;
        }
    },
    MX4(-4F) {
        @Override
        public Rate next() {
            return MX2;
        }

        @Override
        public Rate previous() {
            return MX8;
        }
    },
    MX2(-2F) {
        @Override
        public Rate next() {
            return MX1;
        }

        @Override
        public Rate previous() {
            return MX4;
        }
    },
    MX1(-1F) {
        @Override
        public Rate next() {
            return MX1D2;
        }

        @Override
        public Rate previous() {
            return MX2;
        }
    },
    MX1D2(-1 / 2F) {
        @Override
        public Rate next() {
            return MX1D4;
        }

        @Override
        public Rate previous() {
            return MX1;
        }
    },
    MX1D4(-1 / 4F) {
        @Override
        public Rate next() {
            return MX1D8;
        }

        @Override
        public Rate previous() {
            return MX1D2;
        }
    },
    MX1D8(-1 / 8F) {
        @Override
        public Rate next() {
            return MX1D16;
        }

        @Override
        public Rate previous() {
            return MX1D4;
        }
    },
    MX1D16(-1 / 16F) {
        @Override
        public Rate next() {
            return MX1D32;
        }

        @Override
        public Rate previous() {
            return MX1D8;
        }
    },
    MX1D32(-1 / 32F) {
        @Override
        public Rate next() {
            return X0;
        }

        @Override
        public Rate previous() {
            return MX1D16;
        }
    },
    X0(0) {
        @Override
        public Rate next() {
            return X1D32;
        }

        @Override
        public Rate previous() {
            return MX1D32;
        }
    },
    X1D32(1 / 32F) {
        @Override
        public Rate next() {
            return X1D16;
        }

        @Override
        public Rate previous() {
            return X0;
        }
    },
    X1D16(1 / 16F) {
        @Override
        public Rate next() {
            return X1D8;
        }

        @Override
        public Rate previous() {
            return X1D32;
        }
    },
    X1D8(1 / 8F) {
        @Override
        public Rate next() {
            return X1D4;
        }

        @Override
        public Rate previous() {
            return X1D16;
        }
    },
    X1D4(1 / 4F) {
        @Override
        public Rate next() {
            return X1D2;
        }

        @Override
        public Rate previous() {
            return X1D8;
        }
    },
    X1D2(1 / 2F) {
        @Override
        public Rate next() {
            return X1;
        }

        @Override
        public Rate previous() {
            return X1D4;
        }
    },
    X1(1F) {
        @Override
        public Rate next() {
            return X2;
        }

        @Override
        public Rate previous() {
            return X1D2;
        }
    },
    X2(2F) {
        @Override
        public Rate next() {
            return X4;
        }

        @Override
        public Rate previous() {
            return X1;
        }
    },
    X4(4F) {
        @Override
        public Rate next() {
            return X8;
        }

        @Override
        public Rate previous() {
            return X2;
        }
    },
    X8(8F) {
        @Override
        public Rate next() {
            return X16;
        }

        @Override
        public Rate previous() {
            return X4;
        }
    },
    X16(16F) {
        @Override
        public Rate next() {
            return X32;
        }

        @Override
        public Rate previous() {
            return X8;
        }
    },
    X32(32F) {
        @Override
        public Rate next() {
            return this;
        }

        @Override
        public Rate previous() {
            return X16;
        }
    };

    private float rate;

    Rate(float rate) { this.rate = rate; }

    /**
     * The Value of the Rate
     *
     * @return value off the rate
     */
    public float getValue() { return this.rate; }

    public static Rate getRate(float value) {
        for (Rate rate : Rate.values()) {
            if (rate.getValue() == value) {
                return rate;
            }
        }
        return playRate(); //TODO: double check this method
    }

    /**
     * The Play Rate
     *
     * @return X1 Rate
     */
    public static Rate playRate() { return X1; }

    /**
     * The stop Rate (X0)
     *
     * @return X0 Rate
     */
    public static Rate stopRate() { return X0; }

    /**
     * Next Rate
     *
     * @return next Rate
     */
    public abstract Rate next();

    /**
     * Previous Rate
     *
     * @return previous Rate
     */
    public abstract Rate previous();

}
