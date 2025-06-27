<template>

    <v-card outlined>
        <v-card-title>
            RegisterUser
        </v-card-title>

        <v-card-text>
            <String label="AccountId" v-model="value.accountId" :editMode="editMode"/>
            <String label="Password" v-model="value.password" :editMode="editMode"/>
            <String label="Nickname" v-model="value.nickname" :editMode="editMode"/>
            <Boolean label="AgreedToMarketing" v-model="value.agreedToMarketing" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="registerUser"
            >
                RegisterUser
            </v-btn>
            
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="close"
            >
                Close
            </v-btn>
        </v-card-actions>
    </v-card>

</template>

<script>

export default {
    name: 'RegisterUserCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.accountId = '';
        this.value.password = '';
        this.value.nickname = '';
        this.value.agreedToMarketing = false;
    },
    watch: {
    },
    methods: {
        registerUser() {
            this.$emit('registerUser', this.value);
        },
        close() {
            this.$emit('closeDialog');
        },
        change() {
            this.$emit("update:modelValue", this.value);
        },
    }
}
</script>

